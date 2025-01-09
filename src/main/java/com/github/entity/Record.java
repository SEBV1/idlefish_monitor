package com.github.entity;

import java.time.LocalDateTime;

public class Record {
    private String content;
    private LocalDateTime timestamp;

    public Record(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Record{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
