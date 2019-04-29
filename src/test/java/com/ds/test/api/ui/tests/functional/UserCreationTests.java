package com.ds.test.api.ui.tests.functional;

import com.ds.test.api.ui.GenericTestHelper;
import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.pojo.User;
import com.ds.test.api.ui.pages.AllUserPage;
import com.ds.test.api.ui.pages.LoginPage;
import com.ds.test.api.ui.rest.EndPoints;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class UserCreationTests {
    private static WebDriver driver;
    private static LoginPage loginPage;
    private static AllUserPage allUserPage;
    private static GenericWebSteps commands;
    private Logger logger = LoggerFactory.getLogger(UserCreationTests.class);
    private static int cnt = 0;

    public static String randomString(int length, boolean useLetters, boolean useNumbers) {
        return randomString(length, useLetters, useNumbers);
    }

    @BeforeAll
    public static void initWebDriver() {
        WebDriverManager.getInstance(CHROME).setup();
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        commands = new GenericWebSteps(driver);
        RestAssured.baseURI = PageConstants.NEWUSER.getUrl();

        given()
                .delete(EndPoints.DELETEALLUSERS.getResource())
                .then()
                .statusCode(200);
    }

    @BeforeEach
    public void init() {
        driver.get(PageConstants.NEWUSER.getUrl());
    }

    @Epic("LoginPage Functional Test")
    @Feature("Test registration with diff unique credentials")
    @Story("Creation New User")
    @ParameterizedTest
    @CsvSource(
            {
                    "A,a@db.com,1234567",
                    "B,b@db.com,1234567",
                    "1234djfndnsdmddd,_339dmsmdff@db.com,8374#$%$@&*@$^@! 3()&&#",
                    "1234,_@db.com,******///////#",
            }
    )
    public void createNewUserWithUniqueValues(ArgumentsAccessor argumentsAccessor) {
        String name = argumentsAccessor.get(0).toString();
        String email = argumentsAccessor.get(1).toString();
        String password = argumentsAccessor.get(2).toString();
        User newUser = new User(
                name,
                email,
                password
        );
        loginPage.createNewUser(newUser);
    }

    @Epic("LoginPage Functional Test")
    @Feature("Positive:Test registration with diff unique credentials")
    @Story("UserPassword within [6;255] range")
    @ParameterizedTest
    @ValueSource(ints = {6, 100, 254, 255})
    public void testIfPasswordWithIntMaxLenghtIsPossible(int number) {
        String randomString = GenericTestHelper.randomString(number, true, true);
        logger.info("Entering password with length:\n" + randomString.length());
        User newUser = new User(
                "Alex" + cnt,
                "alex" + cnt + "@xx.com",
                randomString);
        loginPage.createNewUser(newUser);
        cnt+=1;
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative:Test registration with diff unique credentials")
    @Story("UserPassword out of [6;255] range")
    @ParameterizedTest
    @ValueSource(ints = {0, 5, 256})
    @DisplayName("Negative: Entering password at out of limit size value")
    public void testIfPasswordSizeOutOfBoundariesIsPossible(int number) {
        String randomString = GenericTestHelper.randomString(number, true, true);
        logger.info("Entering password with length:\n" + randomString.length());
        User newUser = new User(
                "Alex" + cnt,
                "alex" + cnt + "@xx.com",
                randomString);
        loginPage.createNewUser(newUser);
        switch (number) {
            case 0:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.PASSWORD_REQUIRED)));
                break;
            case 5:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.PASSWORD_ERROR_MIN)));
                break;
            case 256:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.PASSWORD_ERROR_MAX)));
                break;
        }
        cnt=+1;
    }

    @AfterAll
    public static void clearDown() {
        driver.close();
    }
}
