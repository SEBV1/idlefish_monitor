package com.github.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
public class Keywords {
    private String id;
    private String value;
    private Long createTime;

    public Keywords(String id, String value, Long createTime) {
        this.id = id;
        this.value = value;
        this.createTime = createTime;
    }
}
