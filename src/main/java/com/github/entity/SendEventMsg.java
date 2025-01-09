package com.github.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SendEventMsg {
    private Object data;
    private int code;
}
