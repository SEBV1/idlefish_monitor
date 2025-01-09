package com.github.web;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Controller
public class KeywordsController {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEYWORDS_KEY = "keywords";
    private static final String FILTER_PREFIX = "keywords_";
    private static final String FILTER_SUFFIX = "_filter";

    public KeywordsController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @RequestMapping("/keywords/save")
    @ResponseBody
    public ResponseResult<?> save(String value) {
        try {
            List<Keywords> keywordsList = getKeywordsListFromRedis();
            if (isDuplicate(keywordsList, value)) {
                return ResponseResult.failure("重复添加关键字");
            }
            saveKeyWords(value, keywordsList);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.failure("保存关键字失败");
        }
    }


    private List<Keywords> getKeywordsListFromRedis() {
        String json = (String) redisTemplate.opsForValue().get(KEYWORDS_KEY);
        if (json != null) {
            return JSONObject.parseObject(json, new TypeReference<List<Keywords>>() {
            });
        }
        return new ArrayList<>();
    }

    private boolean isDuplicate(List<Keywords> keywordsList, String value) {
        return keywordsList.stream().anyMatch(item -> item.getValue().equals(value));
    }


    private Keywords createKeywords(String value) {
        return Keywords.builder()
            .id(UUID.randomUUID().toString().replaceAll("-", ""))
            .value(value)
            .createTime(System.currentTimeMillis() / 1000)
            .build();
    }

    private void saveKeyWords(String value, List<Keywords> keywordsList) {
        Keywords keywords = createKeywords(value);
        keywordsList.add(keywords);
        String json = JSONObject.toJSONString(keywordsList);
        redisTemplate.opsForValue().set("keywords", json);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.keyword = value;
        Map<String, Object> extraFilterValueBuilder = MapUtil.builder(new HashMap<String, Object>()).build();
        extraFilterValueBuilder.put("hideExcludeMultiPlacesSellers", true);
        extraFilterValueBuilder.putIfAbsent("divisionList", new ArrayList<>());
        searchRequest.extraFilterValue = JSONObject.toJSONString(extraFilterValueBuilder);
        json = JSONObject.toJSONString(searchRequest);
        String key = "keywords_" + keywords.getId() + "_filter";
        redisTemplate.opsForValue().set(key, json);
    }


    /**
     * 查询关键字
     */
    @RequestMapping("/keywords/listData")
    @ResponseBody
    public ResponseResult<?> listData() {
        String json = (String) redisTemplate.opsForValue().get("keywords");
        if (json != null) {
            List<Keywords> keywordsList = JSONObject.parseObject(json, new TypeReference<List<Keywords>>() {
            });
            return ResponseResult.success(keywordsList);
        }
        return ResponseResult.success();
    }


    /**
     * 修改关键字
     */
    @RequestMapping("/keywords/update")
    @ResponseBody
    public ResponseResult<?> update(Keywords keywords) {
        List<Keywords> keywordsList = getKeywordsListFromRedis();
        keywordsList.stream().filter(item -> item.getId().equals(keywords.getId())).findFirst().ifPresent(item -> item.setValue(keywords.getValue()));
        redisTemplate.opsForValue().set("keywords", JSONObject.toJSONString(keywordsList));
        return ResponseResult.success();
    }

    /**
     * 关键字管理页面
     */
    @RequestMapping("/keywords/list")
    public String list() {
        return "/keywords/list";
    }


    /**
     * 增加关键字页面
     */
    @RequestMapping("/keywords/add")
    public String add() {
        return "/keywords/add";
    }

    /**
     * 修改关键字页面
     */
    @RequestMapping("/keywords/edit")
    public String edit(Model model, String id) {
        List<Keywords> keywordsList = getKeywordsListFromRedis();
        Optional<Keywords> optionalKeywords = Optional.ofNullable(keywordsList)
            .flatMap(list -> list.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst());
        optionalKeywords.ifPresent(keywords -> model.addAttribute("keywords", keywords));
        return "/keywords/edit";
    }


    /**
     * 高级设置页面
     */
    @RequestMapping("/keywords/senior")
    public String senior(Model model, String id) {
        addAttributeSearchMethod(model);
        addAttributeCredit(model);
        addAttributePublishDays(model);

        List<Keywords> keywordsList = getKeywordsListFromRedis();
        assert keywordsList != null;
        Keywords keywords = keywordsList.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("keywords", keywords);

        String key = "keywords_" + id + "_filter";
        String json = (String) redisTemplate.opsForValue().get(key);
        model.addAttribute("pageNumber", json == null ? 1 : JSONObject.parseObject(json, SearchRequest.class).pageNumber);

        key = "keywords_title_" + id + "_filter";
        String titleFilter = (String) redisTemplate.opsForValue().get(key);
        model.addAttribute("titleFilter", titleFilter);

        key = "keywords_detail_" + id + "_filter";
        String detailFilter = (String) redisTemplate.opsForValue().get(key);
        model.addAttribute("detailFilter", detailFilter);

        key = "keywords_sell_" + id + "_filter";
        String sellFilter = (String) redisTemplate.opsForValue().get(key);
        model.addAttribute("sellFilter", sellFilter);
        if (json != null) {
            SearchRequest searchRequest = JSONObject.parseObject(json, SearchRequest.class);
            String searchMethodSelected = searchRequest.sortField + "|" + searchRequest.sortValue;
            model.addAttribute("searchMethodSelected", searchMethodSelected);

            if (searchRequest.extraFilterValue != null) {
                JSONArray divisionList = JSONObject.parseObject(searchRequest.extraFilterValue).getJSONArray("divisionList");
                String region = "";
                if (divisionList != null && !divisionList.isEmpty()) {
                    JSONObject divisionListJSONObject = divisionList.getJSONObject(0);
                    if (divisionListJSONObject.size() == 1) {
                        region = divisionListJSONObject.getString("province");
                    } else if (divisionListJSONObject.size() == 2) {
                        region = divisionListJSONObject.getString("province") + "|" + divisionListJSONObject.getString("city");
                    } else if (divisionListJSONObject.size() == 3) {
                        region = divisionListJSONObject.getString("province") + "|" + divisionListJSONObject.getString("city") + "|" + divisionListJSONObject.getString("area");
                    }
                }
                model.addAttribute("region", region);
            }
            processSearchFilter(searchRequest, model);
        }
        return "/keywords/senior";
    }

    public void processSearchFilter(SearchRequest searchRequest, Model model) {
        Optional.ofNullable(searchRequest.propValueStr)
            .map(JSONObject::parseObject)
            .map(json -> json.getString("searchFilter"))
            .map(this::splitAndProcessSearchFilter)
            .ifPresent(result -> result.forEach(model::addAttribute));
    }

    private java.util.Map<String, String> splitAndProcessSearchFilter(String searchFilter) {
        Map<String, String> result = new HashMap<>();
//        String[] split = searchFilter.split(";");
//        for (String value : split) {
//            processSearchFilterValue(value, result);
//        }
        Stream.of(searchFilter.split(";")).forEach(item -> processSearchFilterValue(item, result));
        return result;
    }

    private void processSearchFilterValue(String value, java.util.Map<String, String> result) {
        if (value.contains("priceRange")) {
            result.put("priceRange", extractPriceRange(value));
        } else if (value.contains("publishDays")) {
            result.put("publishDaysSelected", value);
        } else {
            result.put("creditSellerFilter", value);
        }
    }

    private String extractPriceRange(String value) {
        return value.split(":")[1].replaceAll(",", "|");
    }

    private static void addAttributeCredit(Model model) {
        ArrayList<Map<Object, Object>> credit = ListUtil.toList(
            MapUtil.builder().put("value", "creditSellerFilter:creditSellerExcellent").put("name", "信用极好").build(),
            MapUtil.builder().put("value", "creditSellerFilter:creditSellerBetter").put("name", "信用优秀").build());
        model.addAttribute("credit", credit);
    }

    private static void addAttributePublishDays(Model model) {
        ArrayList<Map<Object, Object>> publishDays = ListUtil.toList(
            MapUtil.builder().put("value", "publishDays:1").put("name", "1天内").build(),
            MapUtil.builder().put("value", "publishDays:3").put("name", "3天内").build(),
            MapUtil.builder().put("value", "publishDays:7").put("name", "7天内").build(),
            MapUtil.builder().put("value", "publishDays:14").put("name", "14天内").build());
        model.addAttribute("publishDays", publishDays);
    }

    private static void addAttributeSearchMethod(Model model) {
        ArrayList<Map<Object, Object>> searchMethod = ListUtil.toList(
            MapUtil.builder().put("value", "modify|desc").put("name", "最近活跃").build(),
            MapUtil.builder().put("value", "pos|asc").put("name", "离我最近").build(),
            MapUtil.builder().put("value", "credit|credit_desc").put("name", "信用排序").build());
        model.addAttribute("searchMethod", searchMethod);
    }


    /**
     * 删除关键字
     */
    @RequestMapping("/keywords/del")
    @ResponseBody
    public ResponseResult<?> del(String id) {
        List<Keywords> keywordsList = getKeywordsListFromRedis();
        assert keywordsList != null;
        keywordsList.removeIf(item -> item.getId().equals(id));
        if (keywordsList.isEmpty()) {
            redisTemplate.delete("keywords");
        } else {
            redisTemplate.opsForValue().set("keywords", JSON.toJSONString(keywordsList));
        }

        String[] filterKeys = {
            FILTER_PREFIX + id + "_filter",
            FILTER_PREFIX + "title_" + id + FILTER_SUFFIX,
            FILTER_PREFIX + "detail_" + id + FILTER_SUFFIX,
            FILTER_PREFIX + "sell_" + id + FILTER_SUFFIX
        };
        deleteRedisKeys(filterKeys);
//        String key = "keywords_" + id + "_filter";
//        redisTemplate.delete(key);
//
//        key = "keywords_title_" + id + "_filter";
//        redisTemplate.delete(key);
//
//        key = "keywords_detail_" + id + "_filter";
//        redisTemplate.delete(key);
//
//        key = "keywords_sell_" + id + "_filter";
//        redisTemplate.delete(key);
        return ResponseResult.success();
    }


    private void deleteRedisKeys(String... keys) {
        for (String key : keys) {
            if (key != null) {
                redisTemplate.delete(key);
            }
        }
    }

    @RequestMapping("/keywords/filter")
    @ResponseBody
    public ResponseResult<?> filter(Filter filter) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.keyword = filter.getValue();
        searchRequest.pageNumber = filter.getPageNumber();
        if (StringUtils.isNotEmpty(filter.getSearchMethod())) {
            if (filter.getSearchMethod().equals("pos|asc")) {
                Optional<String> gpsJsonOptional = Optional.ofNullable((String)redisTemplate.opsForValue().get("gps"));
                if (!gpsJsonOptional.isPresent()) {
                    return ResponseResult.failure("未添加gps数据");
                }
                Gps gps = JSONObject.parseObject(gpsJsonOptional.get(), Gps.class);
                searchRequest.gps = gps.getLatitude() + "," + gps.getLongitude();
            }
            String[] parts = filter.getSearchMethod().split("\\|");
            searchRequest.sortField = parts[0];
            searchRequest.sortValue = parts[1];
        }


        Map<String, Object> extraFilterValueBuilder = MapUtil.builder(new HashMap<String, Object>()).build();
        // 处理区域
        handleRegion(filter, extraFilterValueBuilder);

        String searchFilter = getSearchFilter(filter);
        if (!searchFilter.isEmpty()) {
            searchFilter = searchFilter.substring(0, searchFilter.length() - 1);
            Map<String, String> searchFilterValue = MapUtil.builder(new HashMap<String, String>()).put("searchFilter", searchFilter).build();
            searchRequest.propValueStr = JSONObject.toJSONString(searchFilterValue);

            extraFilterValueBuilder.put("hideExcludeMultiPlacesSellers", true);
            extraFilterValueBuilder.putIfAbsent("divisionList", new ArrayList<>());
        }

        searchRequest.extraFilterValue = extraFilterValueBuilder.isEmpty() ? null : JSONObject.toJSONString(extraFilterValueBuilder);
        String json = JSONObject.toJSONString(searchRequest);
        String key = "keywords_" + filter.getId() + "_filter";

        redisTemplate.opsForValue().set(key, json);

        if (StringUtils.isNotEmpty(filter.getTitleFilter())) {
            key = "keywords_title_" + filter.getId() + "_filter";
            redisTemplate.opsForValue().set(key, filter.getTitleFilter());
        }
        if (StringUtils.isNotEmpty(filter.getDetailFilter())) {
            key = "keywords_detail_" + filter.getId() + "_filter";
            redisTemplate.opsForValue().set(key, filter.getDetailFilter());
        }
        if (StringUtils.isNotEmpty(filter.getSellFilter())) {
            key = "keywords_sell_" + filter.getId() + "_filter";
            redisTemplate.opsForValue().set(key, filter.getSellFilter());
        }
        return ResponseResult.success();
    }

    private void handleRegion(Filter filter, Map<String, Object> extraFilterValueBuilder) {
        String region = filter.getRegion();
        if (StringUtils.isNotBlank(region)) {
            List<Map<String, String>> parsedRegionList = parseRegion(region);
            extraFilterValueBuilder.put("divisionList", parsedRegionList);
        }
    }

    private List<Map<String, String>> parseRegion(String region) {
        String[] split = region.split("\\|");
        List<Map<String, String>> list = new ArrayList<>();
        MapBuilder<String, String> regionMap = MapUtil.builder(new HashMap<>());
        switch (split.length) {
            case 1:
                regionMap.put("province", split[0]);
                break;
            case 2:
                regionMap.put("province", split[0]).put("city", split[1]);
                break;
            case 3:
                regionMap.put("province", split[0]).put("city", split[1]).put("area", split[2]);
                break;
            default:
                break;
        }
        list.add(regionMap.build());
        return list;
    }

    private static String getSearchFilter(Filter filter) {
        String priceRange = "";
        String publishDays = "";
        String credit = "";

        if (!filter.getPublishDays().isEmpty()) {
            publishDays = filter.getPublishDays() + ";";
        }
        if (!filter.getCredit().isEmpty()) {
            credit = filter.getCredit() + ";";
        }
        if (filter.getPriceRange() != null && !filter.getPriceRange().isEmpty()) {
            String[] split = filter.getPriceRange().split("\\|");
            if (split.length == 2) {
                priceRange = "priceRange:" + split[0] + "," + split[1] + ";";
            }
        }
        return priceRange + publishDays + credit;
    }
}
