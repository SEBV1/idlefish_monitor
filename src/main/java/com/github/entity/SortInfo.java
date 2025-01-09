package com.github.entity;

import cn.hutool.core.collection.ListUtil;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class SortInfo {


    private String sortField;
    private String sortValue;
    private String name;
    private int parentId;
    private List<SortInfo> children = new ArrayList<>();
    private int id;

    public SortInfo() {
    }

    public SortInfo(String sortField, String sortValue, String name, int parentId, List<SortInfo> children, int id) {
        this.sortField = sortField;
        this.sortValue = sortValue;
        this.name = name;
        this.parentId = parentId;
        this.children = children;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<SortInfo> getChildren() {
        return children;
    }

    public void setChildren(List<SortInfo> children) {
        this.children = children;
    }
}
