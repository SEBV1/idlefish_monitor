package com.github.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@NoArgsConstructor
@Getter
@Setter
public class SearchView {
    private String area;
    private String picUrl;
    private BigDecimal price;
    private String userNickName;
    private Long itemId;
    private String title;
    private String keyword;
    private String publishTime;

    public SearchView(String area, String picUrl, BigDecimal price, String userNickName, Long itemId, String title, String keyword, String publishTime) {
        this.area = area;
        this.picUrl = picUrl;
        this.price = price;
        this.userNickName = userNickName;
        this.itemId = itemId;
        this.title = title;
        this.keyword = keyword;
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "SearchView{" +
            "area='" + area + '\'' +
            ", picUrl='" + picUrl + '\'' +
            ", price='" + price + '\'' +
            ", userNickName='" + userNickName + '\'' +
            ", itemId='" + itemId + '\'' +
            ", title='" + title + '\'' +
            ", keyword='" + keyword + '\'' +
            ", publishTime='" + publishTime + '\'' +
            '}';
    }
}
