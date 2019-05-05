package com.ds.tests.nexttorun.functional;


import com.ds.pojo.User;
import com.ds.test.api.rest.RestBaseFunctionalTest;
import com.ds.test.api.utility.GenericTestHelper;
import com.ds.test.api.rest.EndPoints;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RestApiFunctionalTests extends RestBaseFunctionalTest {

    private final static String randomStr = GenericTestHelper.randomString(4, true, true);

    @BeforeEach
    public void deleteAllUsers() {
        Assert.assertThat(RestBaseFunctionalTest.deleteUsers(), is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
    }

    @Epic("RestAPI Smoke Test")
    @Story("HTTP Code is to be 200 for GET /new/user")
    @Test
    public void testNewUserEndPoint() {
        given()
                .get(EndPoints.NEWUSER.getResource())
                .then()
                .statusCode(RestBaseFunctionalTest.HTTP_OK);
    }

    @Epic("RestAPI Smoke Test")
    @Story("HTTP Code is to be 200 for GET /user/all/json and returns JSON object")
    @Test
    public void testUserAllJsonEndPoint() {
        given()
                .get(EndPoints.USERALLJSON.getResource())
                .then()
                .statusCode(RestBaseFunctionalTest.HTTP_OK);
    }

    @Issue("Fail to save User due to the issue: if (!user.password.equals(confirmationPassword))")
    @Severity(SeverityLevel.NORMAL)
    @Epic("RestAPI Functional Test")
    @Story("HTTP Code is to be 200 for POST /user/save and returns JSON object")
    @Test
    public void testUserSaveEndPoint() {
        User newUser = new User(
                "123" + randomStr,
                "123@d.com" + randomStr,
                randomStr
        );
        given()
                .body(newUser)
                .post(EndPoints.USERSAVE.getResource())
                .then().statusCode(RestBaseFunctionalTest.HTTP_OK);
        Response response = given()
                .get(EndPoints.USERALLJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
        User user = GenericTestHelper.deserialization(response);
        Assertions.assertAll(
                () -> Assert.assertThat(user.getName(), Is.is(equalTo(newUser.getName()))),
                () -> Assert.assertThat(user.getEmail(), Is.is(equalTo(newUser.getEmail()))),
                () -> Assert.assertThat(user.getPassword(), Is.is(equalTo(newUser.getPassword()))),
                () -> Assert.assertNotNull(user.getId())
        );
    }

    @Epic("RestAPI Functional Test")
    @Story("HTTP Code is to be 200 for POST /user/save/json and returns JSON object")
    @Test
    public void testUserSaveJsonEndPoint() {
        User newUser = new User(
                "555" + randomStr,
                "555@d.com" + randomStr,
                randomStr
        );
        given()
                .body(newUser)
                .post(EndPoints.USERSAVEJSON.getResource())
                .then().statusCode(RestBaseFunctionalTest.HTTP_OK);
        Response response = given()
                .get(EndPoints.USERALLJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
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
    @Epic("RestAPI Functional Test")
    @Story("Negative: HTTP Code is not to be 200 for POST /user/all/json with name missed")
    @Test
    public void testUserSaveJsonIsNotSaveDueToNameMissed() {
        User newUser = new User(
                "",
                randomStr + "@d.com",
                randomStr
        );
        Response response = given()
                .body(newUser)
                .post(EndPoints.USERSAVEJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_FAILED)));

    }

    @Issue("User can be saved with NULL email")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("RestAPI Functional Test")
    @Story("Negative: HTTP Code is not to be 200 for POST /user/all/json with email missed")
    @Test
    public void testUserSaveJsonIsNotSaveDueToEmailMissed() {
        User newUser = new User(
                randomStr,
                "",
                randomStr
        );
        Response response = given()
                .body(newUser)
                .post(EndPoints.USERSAVEJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_FAILED)));
    }

    @Issue("User can be saved with NULL password")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("RestAPI Functional Test")
    @Story("Negative: HTTP Code is not to be 200 for POST /user/all/json with password missed")
    @Test
    public void testUserSaveJsonIsNotSaveDueToPasswordMissed() {
        User newUser = new User(
                randomStr,
                randomStr + "@r.com",
                ""
        );
        Response response = given()
                .body(newUser)
                .post(EndPoints.USERSAVEJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_FAILED)));
    }

    @Issue("User can be saved with all empty credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("RestAPI Functional Test")
    @Story("Negative: HTTP Code is not to be 200 for POST /user/all/json with all data missed")
    @Test
    public void testUserSaveJsonIsNotSaveDueToAllDataMissed() {
        User newUser = new User(
                "",
                "",
                ""
        );
        given()
                .body(newUser)
                .post(EndPoints.USERSAVEJSON.getResource());
        Response response = given()
                .get(EndPoints.USERALLJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
        User user = GenericTestHelper.deserialization(response);
        Assertions.assertAll(
                () -> Assert.assertThat(user.getName(), Is.is(equalTo(""))),
                () -> Assert.assertThat(user.getEmail(), Is.is(equalTo(""))),
                () -> Assert.assertThat(user.getPassword(), Is.is(equalTo(""))),
                () -> Assert.assertNotNull(user.getId())
        );
    }

    @AfterAll
    public static void deleteAllTestUsers() {
        /**
         * Delete all test user created during the tests since there are left user records with blank fields
         */
        Assert.assertThat(RestBaseFunctionalTest.deleteUsers(), is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
    }
}
