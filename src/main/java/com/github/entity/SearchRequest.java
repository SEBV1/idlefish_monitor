package com.github.entity;

import com.alibaba.fastjson.JSONObject;

public class SearchRequest {
    public String abConfigs = JSONObject.toJSONString(new AbConfigs());
    public boolean activeSearch = false;
    public String apiName = "com.taobao.idlefish.search_implement.protocol.SearchResultReq";
    public String bizFrom = "home";
    public int connTimeoutMilliSecond = 0;
    public int disableHierarchicalSort = 0;
    public String extraFilterValue = null;
    public boolean forceUseInputKeyword = false;
    public boolean forceUseTppRepair = false;
    public boolean fromFilter = false;
    public boolean fromKits = false;
    public boolean fromLeaf = false;
    public boolean fromShade = false;
    public boolean fromSuggest = false;
    public String gps = null;
    public String keyword = "";
    public boolean mainTab = true;
    public boolean originJson = false;
    public int page = 1;
    public int pageNumber = 1;
    public String propValueStr = null;
    public int relateResultListLastIndex = 0;
    public int relateResultPageNumber = 1;
    public int resultListLastIndex = 0;
    public int rowsPerPage = 10;
    public String searchReqFromActivatePagePart = "searchButton";
    public String searchReqFromPage = "xyHome";
    public String searchTabType = "SEARCH_TAB_MAIN";
    public String shadeBucketNum = "-1";
    public boolean smartUIFilter = true;
    public int socketTimeoutMilliSecond = 0;
    public String sortField = null;
    public String sortValue = null;
    public long startTime = System.currentTimeMillis();
    public String suggestBucketNum = "31";
    public boolean supportFlexFilter = true;


    @Override
    public String toString() {
        return "SearchRequest{" +
            "abConfigs='" + abConfigs + '\'' +
            ", activeSearch=" + activeSearch +
            ", apiName='" + apiName + '\'' +
            ", bizFrom='" + bizFrom + '\'' +
            ", connTimeoutMilliSecond=" + connTimeoutMilliSecond +
            ", disableHierarchicalSort=" + disableHierarchicalSort +
            ", extraFilterValue='" + extraFilterValue + '\'' +
            ", forceUseInputKeyword=" + forceUseInputKeyword +
            ", forceUseTppRepair=" + forceUseTppRepair +
            ", fromFilter=" + fromFilter +
            ", fromKits=" + fromKits +
            ", fromLeaf=" + fromLeaf +
            ", fromShade=" + fromShade +
            ", fromSuggest=" + fromSuggest +
            ", gps='" + gps + '\'' +
            ", keyword='" + keyword + '\'' +
            ", mainTab=" + mainTab +
            ", originJson=" + originJson +
            ", page=" + page +
            ", pageNumber=" + pageNumber +
            ", propValueStr='" + propValueStr + '\'' +
            ", relateResultListLastIndex=" + relateResultListLastIndex +
            ", relateResultPageNumber=" + relateResultPageNumber +
            ", resultListLastIndex=" + resultListLastIndex +
            ", rowsPerPage=" + rowsPerPage +
            ", searchReqFromActivatePagePart='" + searchReqFromActivatePagePart + '\'' +
            ", searchReqFromPage='" + searchReqFromPage + '\'' +
            ", searchTabType='" + searchTabType + '\'' +
            ", shadeBucketNum='" + shadeBucketNum + '\'' +
            ", smartUIFilter=" + smartUIFilter +
            ", socketTimeoutMilliSecond=" + socketTimeoutMilliSecond +
            ", sortField='" + sortField + '\'' +
            ", sortValue='" + sortValue + '\'' +
            ", startTime=" + startTime +
            ", suggestBucketNum='" + suggestBucketNum + '\'' +
            ", supportFlexFilter=" + supportFlexFilter +
            '}';
    }
}
