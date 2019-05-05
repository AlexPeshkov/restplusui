package com.ds.test.api.rest;

public enum EndPoints {
    NEWUSER("/user/new"),
    USERALLJSON("/user/all/json"),
    USERSAVE("/user/save"),
    USERSAVEJSON("/user/save/json"),
    USERSRALL("/users/all"),
    USERSRALLTODELETE("/user/all");

    public String getResource() {
        return resource;
    }

    private String resource;

    EndPoints(String resource) {
        this.resource = resource;
    }
}
