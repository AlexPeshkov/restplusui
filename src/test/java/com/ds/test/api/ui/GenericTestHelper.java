package com.ds.test.api.ui;

import com.ds.test.api.ui.pages.LoginPage;
import com.ds.test.api.ui.pojo.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class GenericTestHelper {

    public static String randomString(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    @Step
    public static void isRegistrationFormFilled(User user, LoginPage loginPage) {
        Assertions.assertAll(
                () -> Assert.assertThat(loginPage.userName.getAttribute("value"), is(equalTo(user.getName()))),
                () -> Assert.assertThat(loginPage.userEmail.getAttribute("value"), is(equalTo(user.getEmail()))),
                () -> Assert.assertThat(loginPage.userPassword.getAttribute("value"), is(equalTo(user.getPassword())))
        );
    }

    public static void switchWindow(WebDriver webDriver) {
        String winHandleBefore = webDriver.getWindowHandle();
        for (String winHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(winHandle);
        }
    }

    public static User deserialization(Response response) {
        Gson gson = new Gson();
        User user = null;
        JsonParser parser = new JsonParser();
        JsonArray object = (JsonArray) parser.parse(response.asString());
        for (int i = 0; i < object.size(); i++) {
            JsonObject jsonObject = object.get(i).getAsJsonObject();
            user = gson.fromJson(jsonObject, User.class);
        }
        return user;
    }
}
