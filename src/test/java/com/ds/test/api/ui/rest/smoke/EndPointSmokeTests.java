package com.ds.test.api.ui.rest.smoke;


import com.ds.test.api.ui.GenericTestHelper;
import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.pojo.User;
import com.ds.test.api.ui.rest.EndPoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.internal.assertion.AssertionSupport;
import io.restassured.response.Response;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class EndPointSmokeTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = PageConstants.NEWUSER.getUrl();
    }

    @BeforeEach
    public void deleteAllUsers() {
        given()
                .delete(EndPoints.DELETEALLUSERS.getResource())
                .then()
                .statusCode(200);
    }

    @Epic("RestAPI Smoke Test")
    @Story("HTTP Code is to be 200 for GET /new/user")
    @Test
    public void testNewUserEndPoint() {
        given()
                .get(EndPoints.GETNEWUSER.getResource())
                .then()
                .statusCode(200);
    }

    @Epic("RestAPI Smoke Test")
    @Story("HTTP Code is to be 200 for GET /user/all/json and returns JSON object")
    @Test
    public void testUserAllJsonEndPoint() {
        given()
                .get(EndPoints.GETUSERJSON.getResource())
                .then()
                .statusCode(200);
    }

    @Epic("RestAPI Smoke Test")
    @Story("HTTP Code is to be 200 for POST /user/all/json and returns JSON object")
    @Test
    public void testUserSaveJsonEndPoint() {
        String randomStr = GenericTestHelper.randomString(10, true, true);
        User newUser = new User(
                "555" + randomStr,
                "555@d.com" + randomStr,
                randomStr
        );
        given()
                .body(newUser)
                .post(EndPoints.POSTUSERSAVEJSON.getResource())
                .then().statusCode(200);
        Response response = given()
                .get(EndPoints.GETUSERJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(200)));
        User user = GenericTestHelper.deserialization(response);
        Assertions.assertAll(
                () -> Assert.assertThat(user.getName(), Is.is(equalTo(newUser.getName()))),
                () -> Assert.assertThat(user.getEmail(), Is.is(equalTo(newUser.getEmail()))),
                () -> Assert.assertThat(user.getPassword(), Is.is(equalTo(newUser.getPassword()))),
                () -> Assert.assertNotNull(user.getId())
        );
    }


    @Issue("User can be saved with NULL name")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("RestAPI Smoke Test")
    @Story("Negative: HTTP Code is not to be 200 for POST /user/all/json with name missed")
    @Test
    public void testUserSaveJsonIsNotSaveDueToMissingData() {
        String randomStr = GenericTestHelper.randomString(4, true, true);
        User newUser = new User(
                "",
                randomStr + "@d.com",
                randomStr
        );
        Response response = given()
                .body(newUser)
                .post(EndPoints.POSTUSERSAVEJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(500)));

    }
}
