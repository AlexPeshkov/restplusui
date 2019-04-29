package com.ds.test.api.ui;

public enum PageConstants {

    NEWUSER("http://85.93.17.135:9000/", "New User"),
    ALLUSERS("http://85.93.17.135:9000/users/all", "All User");

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    private String url;
    private String title;

    PageConstants(String url, String title) {
        this.url = url;
        this.title = title;
    }
}
