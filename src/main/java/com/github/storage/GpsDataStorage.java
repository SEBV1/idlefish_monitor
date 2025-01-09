//package com.github.storage;
//
//import com.github.entity.Gps;
//import com.github.entity.Keywords;
//import com.github.utils.FileUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
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
//@Slf4j
//@Component
//public class GpsDataStorage {
//
//    @Value("${gpsFileUrl}")
//    private String gpsFileUrl;
//
//    public void save(Gps gps) throws IOException {
//        FileUtils.createFile(gpsFileUrl);
//        FileUtils.clearFile(gpsFileUrl);
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                Files.newOutputStream(Paths.get(gpsFileUrl), StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
//            writer.write(gps.getLongitude() + "|" + gps.getLatitude());
//            writer.newLine();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new IOException(e.getMessage());
//        }
//    }
//
//    public Gps loadRecords() throws IOException {
//        FileUtils.createFile(gpsFileUrl);
//        List<Keywords> keywords = new ArrayList<>();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(gpsFileUrl)), StandardCharsets.UTF_8));
//        String line;
//        String readLine = reader.readLine();
//        if (null == readLine) {
//            return null;
//        }
//        String[] parts = readLine.split("\\|");
//        String longitude = parts[0];
//        String latitude = parts[1];
//        return new Gps(longitude, latitude);
//    }
//}
