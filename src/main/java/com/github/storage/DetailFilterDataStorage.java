//package com.github.storage;
//
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
//
//import static com.github.utils.FileUtils.createFile;
//
//@Slf4j
//@Component
//public class DetailFilterDataStorage {
//
//    @Value("${detailFilter}")
//    private String detailFilter;
//
//    public void save(String id, String json) throws IOException {
//        String detailFilterTemp = String.format(detailFilter, id);
//        FileUtils.clearFile(detailFilterTemp);
//        createFile(detailFilterTemp);
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//            Files.newOutputStream(Paths.get(detailFilterTemp), StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
//            writer.write(json);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    public String loadRecords(String id) throws IOException {
//        String detailFilterTemp = String.format(detailFilter, id);
//        FileUtils.createFile(detailFilterTemp);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(detailFilterTemp)), StandardCharsets.UTF_8));
//        return reader.readLine();
//    }
//}
