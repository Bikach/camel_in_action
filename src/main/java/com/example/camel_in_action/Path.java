package com.example.camel_in_action;

public enum Path {
    CHAPTER_1("src/main/java/com/example/camel_in_action/chapter1/"),
    CHAPTER_2("src/main/java/com/example/camel_in_action/chapter2/"),
    CHAPTER_3("src/main/java/com/example/camel_in_action/chapter3/");

    public final String path;

    Path(String path) {
        this.path = path;
    }
}
