package com.gdgoc.website.activity.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Quarter {
    Q1("1분기"),
    Q2("2분기"),
    Q3("3분기");

    private final String label;
}
