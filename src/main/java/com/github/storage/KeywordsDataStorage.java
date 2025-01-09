//package com.github.storage;
//
//import com.github.entity.Keywords;
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
//public class KeywordsDataStorage {
//
//    @Value("${keywordsFileUrl}")
//    private String keywordsFileUrl;
//
//    public void save(Keywords keywords) throws IOException {
//        createFile(keywordsFileUrl);
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                Files.newOutputStream(Paths.get(keywordsFileUrl), StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
//            writer.write(keywords.getId() + "|" + keywords.getValue() + "|" + keywords.getCreateTime());
//            writer.newLine();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    public List<Keywords> loadRecords() throws IOException {
//        List<Keywords> keywords = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(keywordsFileUrl)), StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("\\|");
//                String id = parts[0];
//                String value = parts[1];
//                Long createTime = Long.parseLong(parts[2]);
//                keywords.add(new Keywords(value, id) {
//                    @Override
//                    public Long getCreateTime() {
//                        return createTime;
//                    }
//                });
//            }
//            return keywords;
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            // 文件不存在时，返回空列表
//            return null;
//        }
//    }
//
//
//    public void del(String id) throws IOException {
//        List<Keywords> keywordsList = loadRecords();
//        if (null != keywordsList) {
//            keywordsList.removeIf(keywords -> id.equals(keywords.getId()));
//        }
//        assert keywordsList != null;
//        FileUtils.clearFile(keywordsFileUrl);
//        for (Keywords keywords : keywordsList) {
//            save(keywords);
//        }
//        if (keywordsList.isEmpty()) {
//            FileUtils.clearFile(keywordsFileUrl);
//        }
//
//    }
//
//
//    public void update(Keywords keywords) throws IOException {
//        List<Keywords> keywordsList = loadRecords();
//        for (Keywords item : keywordsList) {
//            if (item.getId().equals(keywords.getId())) {
//                item.setValue(keywords.getValue());
//            }
//        }
//        FileUtils.clearFile(keywordsFileUrl);
//        for (Keywords item : keywordsList) {
//            save(item);
//        }
//    }
//
//    public Keywords getById(String id) throws IOException {
//        List<Keywords> keywordsList = loadRecords();
//        if (null != keywordsList) {
//            for (Keywords keywords : keywordsList) {
//                if (keywords.getId().equals(id)) {
//                    return keywords;
//                }
//            }
//        }
//        return null;
//    }
//
//}
