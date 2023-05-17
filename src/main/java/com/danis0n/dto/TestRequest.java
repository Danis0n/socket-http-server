package com.danis0n.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TestRequest {
    private String name;
    private String password;
}
