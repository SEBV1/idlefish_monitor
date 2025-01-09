package com.github.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static void createFile(String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    log.info("文件创建成功:{}", fileUrl);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void clearFile(String fileUrl) {
        try (FileWriter writer = new FileWriter(fileUrl)) {
            writer.write("");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
