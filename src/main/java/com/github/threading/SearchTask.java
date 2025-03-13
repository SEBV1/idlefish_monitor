package com.github.threading;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.builder.RequestParamBuilder;
import com.github.constants.CommonConstants;
import com.github.entity.Keywords;
import com.github.entity.SearchRequest;
import com.github.entity.SearchView;
import com.github.service.JNICLibraryV7_18_92ServiceWorker;
import com.github.utils.MapConvert;
import com.github.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

@Slf4j
public class SearchTask implements Callable<List<SearchView>> {
    private final JNICLibraryV7_18_92ServiceWorker jnicLibraryV7_18_92ServiceWorker;
    private final RedisTemplate redisTemplate;

    public SearchTask(JNICLibraryV7_18_92ServiceWorker jnicLibraryV7_18_92ServiceWorker, RedisTemplate redisTemplate) {
        this.jnicLibraryV7_18_92ServiceWorker = jnicLibraryV7_18_92ServiceWorker;
        this.redisTemplate = redisTemplate;
    }


    private SearchRequest getSearchRequest(Keywords item) {
        String key = "keywords_" + item.getId() + "_filter";
        String keywordsFilter = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(keywordsFilter)) {
            return null;
        }
        return JSONObject.parseObject(keywordsFilter, SearchRequest.class);
    }


    private String getTitleFilter(Keywords item) {
        String key = "keywords_title_" + item.getId() + "_filter";
        return (String) redisTemplate.opsForValue().get(key);
    }

    private String getSellFilter(Keywords item) {
        String key = "keywords_sell_" + item.getId() + "_filter";
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<SearchView> call() throws Exception {
        List<SearchView> searchViewList = new ArrayList<>();
        String json = (String) redisTemplate.opsForValue().get("keywords");
        if (json != null) {
            List<Keywords> keywordsList = JSONObject.parseObject(json, new TypeReference<List<Keywords>>() {
            });
            for (Keywords item : keywordsList) {
                SearchRequest searchRequest = getSearchRequest(item);
                assert searchRequest != null;
                searchRequest.rowsPerPage = searchRequest.pageNumber * searchRequest.rowsPerPage;
                String titleFilter = getTitleFilter(item);
                String sellFilter = getSellFilter(item);

                String t = String.valueOf(System.currentTimeMillis() / 1000);
                String dataJson = JSONObject.toJSONString(searchRequest);
                Map<String, String> param = RequestParamBuilder.builderParam(t, dataJson, CommonConstants.searchApi);
                String input = MapConvert.convertInnerBaseStrMap(CommonConstants.appKey, param, true).get("INPUT").toString();
                Map<String, String> signResult = jnicLibraryV7_18_92ServiceWorker.doCommandNative_70102(CommonConstants.appKey, input, CommonConstants.searchApi).get();

                try {

                    String cookie = Optional.ofNullable(redisTemplate.opsForValue().get("cookie"))
                            .map(Object::toString)
                            .orElse(null);
                    Map<String, String> headers = RequestParamBuilder.builderHeaders(signResult.get("x-sgext"), signResult.get("x-sign"), signResult.get("x-mini-wua"), t, signResult.get("x-umt"), cookie);
                    param.clear();
                    param.put("data", URLEncoder.encode(dataJson, "UTF-8"));
                    String url = "https://acs.m.goofish.com/gw/mtop.taobao.idlemtopsearch.search/1.0/";
                    JSONArray resultList = JSONObject.parseObject(OkHttpUtil.postForm(url, headers, dataJson)).getJSONObject("data").getJSONArray("resultList");
                    for (int i = 0; i < resultList.size(); i++) {
                        SearchView searchView = buildSearchView(resultList.getJSONObject(i), searchRequest);
                        // 标题关键字过滤
                        if (searchView != null && isCheckTitleFilter(titleFilter, searchView.getTitle()) && isSellFilter(sellFilter, searchView.getUserNickName())) {
                            if (searchView.getUserNickName() != null) {
                                searchViewList.add(searchView);
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return searchViewList;
    }


    private SearchView buildSearchView(JSONObject jsonObject, SearchRequest searchRequest) {
        try {
            Long publishTime = jsonObject.getJSONObject("data").getJSONObject("item").getJSONObject("main").getJSONObject("clickParam").getJSONObject("args").getLong("publishTime");
            String format = null;
            if (publishTime != null) {
                format = DateUtil.format(DateTime.of(publishTime), "yyyy-MM-dd HH:mm:ss");
            }
            JSONObject exContent = jsonObject.getJSONObject("data").getJSONObject("item").getJSONObject("main").getJSONObject("exContent");
            Long itemId = exContent.getLong("itemId");
            if (itemId == null) {
                return null;
            }
            String title = exContent.getString("title");
            String area = exContent.getString("area");
            String picUrl = exContent.getString("picUrl");
            String userNickName = exContent.getString("userNickName");
            BigDecimal price = exContent.getJSONArray("price").getJSONObject(1).getBigDecimal("text");

            return SearchView.builder()
                    .title(title)
                    .area(area)
                    .picUrl(picUrl)
                    .price(price)
                    .itemId(itemId)
                    .userNickName(userNickName)
                    .keyword(searchRequest.keyword)
                    .publishTime(format)
                    .build();
        } catch (Exception e) {
            log.error("Failed to build SearchView from JSON object. JSON: {}", jsonObject, e);
            return null;
        }
    }


    private static boolean isSellFilter(String userNameFilter, String userName) {
        return isCheckFilter(userNameFilter, userName);
    }

    /**
     * 过滤标题关键字
     *
     * @param titleFilter 关键字
     * @param title       标题
     */
    private static boolean isCheckTitleFilter(String titleFilter, String title) {
        return isCheckFilter(titleFilter, title);
    }

    private static boolean isCheckFilter(String titleFilter, String title) {
        boolean checkTitleFilter = true;
        if (StringUtils.isNotBlank(titleFilter)) {
            String[] titleFilterSplit = titleFilter.split("\\|");
            for (String value : titleFilterSplit) {
                if (StringUtils.contains(title, value)) {
                    checkTitleFilter = false;
                    break;
                }
            }
        }
        return checkTitleFilter;
    }
}
