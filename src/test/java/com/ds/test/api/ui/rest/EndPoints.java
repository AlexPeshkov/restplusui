package com.ds.test.api.ui.rest;

public enum EndPoints {
    GET("/"),
    GETNEWUSER("/user/new"),
    GETUSERJSON("/user/all/json"),
    POSTUSERSAVE("/user/save"),
    POSTUSERSAVEJSON("/user/save/json"),
    DELETEALLUSERS("/user/all");

    public String getResource() {
        return resource;
    }

    private String resource;

    EndPoints(String resource) {
        this.resource = resource;
    }

}
