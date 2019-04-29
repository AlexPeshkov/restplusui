package com.ds.test.api.ui.rest;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class RestBuilder {

    public RestBuilder(String resource) {
        RestAssured.baseURI = "http://85.93.17.135";
        RestAssured.basePath = resource;
    }



}
