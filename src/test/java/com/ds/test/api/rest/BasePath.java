package com.ds.test.api.rest;

public enum BasePath {

    BASEURL("http://85.93.17.135:9000");

    private String resource;
    public String getResource() {
        return resource;
    }
    BasePath(String resource) {
        this.resource = resource;
    }
}