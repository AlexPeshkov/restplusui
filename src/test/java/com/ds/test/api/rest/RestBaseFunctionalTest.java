package com.ds.test.api.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class RestBaseFunctionalTest {

    public static final int HTTP_OK = 200;
    public static final int HTTP_FAILED = 500;

    @BeforeAll
    public static void setup() {
        String baseHost = System.getProperty("server.host");
        if(baseHost==null){
            baseHost = BasePath.BASEURL.getResource();
        }
        RestAssured.baseURI = baseHost;
    }

    protected static int deleteUsers() {
        Response response = given()
                .delete(EndPoints.USERSRALLTODELETE.getResource());
        return response.getStatusCode();
    }
}
