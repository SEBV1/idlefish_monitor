//package com.github.storage;
//
//import com.github.entity.SearchView;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//// 定义存储对象的类
//class MyObject {
//    private int id;
//
//    public MyObject(int id) {
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//}
//
//// 定义用于保存不相等数据的类
//class UnequalData {
//    private int id;
//
//    public UnequalData(int id) {
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//}
//
//public class ListIdComparison {
//    public static void main(String[] args) {
//        List<SearchView> searchViewNewDataList = new ArrayList<>();
//
//        SearchView searchView = new SearchView();
//        searchView.setItemId(123456L);
//        searchView.setTitle("新数据1");
//        searchViewNewDataList.add(searchView);
//
//        searchView = new SearchView();
//        searchView.setItemId(1234567L);
//        searchView.setTitle("新数据2");
//        searchViewNewDataList.add(searchView);
//
//
//        List<SearchView> searchViewOldDataList = new ArrayList<>();
//
//        searchView = new SearchView();
//        searchView.setItemId(123456L);
//        searchView.setTitle("旧数据1");
//        searchViewOldDataList.add(searchView);
//
//        searchView = new SearchView();
//        searchView.setItemId(1234568L);
//        searchView.setTitle("旧数据2");
//        searchViewOldDataList.add(searchView);
//
//        List<SearchView> unequalData = findUnequalData(searchViewNewDataList, searchViewOldDataList);
////        saveData(unequalData);
//    }
//
//
//    // 找出不相等的数据
//    private static List<SearchView> findUnequalData(List<SearchView> searchViewNewDataList, List<SearchView> searchViewOldDataList) {
//        Set<Long> idSet = searchViewOldDataList.stream()
//            .map(SearchView::getItemId)
//            .collect(Collectors.toSet());
//        return searchViewNewDataList.stream()
//            .filter(obj -> !idSet.contains(obj.getItemId()))
//            .map(obj -> SearchView.builder().itemId(obj.getItemId()).
//                title(obj.getTitle()).area(obj.getArea()).price(obj.getPrice()).
//                keyword(obj.getKeyword()).picUrl(obj.getPicUrl()).publishTime(obj.getPublishTime()).
//                userNickName(obj.getUserNickName()).build())
//            .collect(Collectors.toList());
//    }
//
//    // 模拟保存数据
//    private static void saveData(List<SearchView> dataList, List<SearchView> searchViewOldDataList) {
//        dataList.forEach(newData -> {
//            Long itemId = newData.getItemId();
//            boolean exists = searchViewOldDataList.stream()
//                .anyMatch(oldData -> oldData.getItemId().equals(itemId));
//            if (!exists) {
//                searchViewOldDataList.add(0, newData);
//            }
//        });
//    }
//}
