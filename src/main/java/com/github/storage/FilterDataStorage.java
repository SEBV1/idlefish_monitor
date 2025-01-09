//package com.github.storage;
//
//import com.alibaba.fastjson.JSONObject;
//import com.github.entity.Gps;
//import com.github.entity.Keywords;
//import com.github.entity.SearchRequest;
//import com.github.utils.FileUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.github.utils.FileUtils.createFile;
//
//@Slf4j
//@Component
//public class FilterDataStorage {
//
//    @Value("${keywordsFilter}")
//    private String keywordsFilter;
//
//    public void save(String id, String filter) throws IOException {
//        String keywordsFilterTemp = String.format(keywordsFilter, id);
//        FileUtils.clearFile(keywordsFilterTemp);
//        createFile(keywordsFilterTemp);
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                Files.newOutputStream(Paths.get(keywordsFilterTemp), StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
//            writer.write(filter);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new IOException();
//        }
//    }
//
//    public SearchRequest loadRecords(String id) throws IOException {
//        String keywordsFilterTemp = String.format(keywordsFilter, id);
//        FileUtils.createFile(keywordsFilterTemp);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(keywordsFilterTemp)), StandardCharsets.UTF_8));
//        String line;
//        String json = reader.readLine();
//        if (null == json) {
//            return null;
//        }
//        SearchRequest searchRequest = JSONObject.parseObject(json, SearchRequest.class);
//        return searchRequest;
//    }
//}
