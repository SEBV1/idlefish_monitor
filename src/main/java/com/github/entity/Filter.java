package com.github.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Filter {
    public String id;
    private String value;
    private String searchMethod;
    private String region;
    private String publishDays;
    private String credit;
    private String priceRange;
    private int pageNumber;
    private String titleFilter;
    private String detailFilter;
    private String sellFilter;
}
