package com.ds.test.api.ui.tests.functional;

import com.ds.test.api.ui.GenericTestHelper;
import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.TestWebDriver;
import com.ds.test.api.ui.pojo.User;
import com.ds.test.api.ui.pages.AllUserPage;
import com.ds.test.api.ui.pages.LoginPage;
import com.ds.test.api.ui.rest.EndPoints;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.List;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class UserCreationTests {

    private final int HTTP_OK = 200;

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static AllUserPage allUserPage;
    private static GenericWebSteps commands;
    private static TestWebDriver testWebDriver;
    private static int cnt = 0;

    @BeforeAll
    public static void initWebDriver() {
        testWebDriver = TestWebDriver.newBuilder()
                .setWebDriverManager(CHROME)
                .build();
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        allUserPage = new AllUserPage(driver);
        commands = new GenericWebSteps(driver);
        RestAssured.baseURI = PageConstants.NEWUSER.getUrl();
    }

    @BeforeEach
    public void init() {
        driver.get(PageConstants.NEWUSER.getUrl());
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative: User can't be created if any field is empty")
    @Story("User name is missed")
    @Test
    public void testIfNameIsRequiredToCreateNewUser(){
        User newUser = new User(
                "",
                "noname@xx.it",
                "1234567"
        );
        loginPage.createNewUser(newUser);
        Assert.assertThat(loginPage.userNameError.getText(), is(equalTo(loginPage.FIELD_IS_REQUIRED)));
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative: User can't be created if any field is empty")
    @Story("User email is empty")
    @Test
    public void testIfEmailIsRequiredToCreateNewUser(){
        User newUser = new User(
                "NoName",
                "",
                "1234567"
        );
        loginPage.createNewUser(newUser);
        Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(loginPage.FIELD_IS_REQUIRED)));
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative: User can't be created if any field is empty")
    @Story("User email is invalid, does not match your@email.com pattern")
    @Test
    public void testIfInvalidEmailIsFineToCreateNewUser(){
        User newUser = new User(
                "NoName",
                "No.email.cz",
                "1234567"
        );
        loginPage.createNewUser(newUser);
        Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(loginPage.INVALID_EMAIL)));
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative: User can't be created if password and password repeat is not the same")
    @Story("Password and Password Repeat are differ")
    @Test
    public void testIfPasswordCanBeDifferToCreateNewUser(){
        User newUser = new User(
                "NoName",
                "k@44.com",
                "1234567"
        );
        loginPage.createNewUser(newUser, "12345677");
        Assert.assertThat(loginPage.userConfirmationPasswordError.getText(), is(equalTo(loginPage.PASSWORD_NOT_THE_SAME)));
    }

    @Issue("Case #3: Password with a space is split by line in UI")
    @Severity(SeverityLevel.MINOR)
    @Epic("LoginPage Functional Test")
    @Feature("Create a new user with unique credentials and user details are available on 'New User' page and Rest API returns correct user details")
    @Story("Creation New User when registration form is filled in properly")
    @ParameterizedTest
    @CsvSource(
            {
                    "A,a@db.com,1234567",
                    "B,b@db.com,1234567",
                    "1234djfndnsdmddd,_339dmsmdff@db.com,8374#$%$@&*@$^@! 3()&&#", //with space (fail)
                    "//\344555553(,9dmsmdff@db.com,8374#$%$@&*@$^@!3()&&#",
                    " 1234,_@db.com,******///////#",
            }
    )
    public void createNewUserWithUniqueValues(ArgumentsAccessor argumentsAccessor) {
        given()
                .delete(EndPoints.DELETEALLUSERS.getResource())
                .then()
                .statusCode(200);
        String name = argumentsAccessor.get(0).toString();
        String email = argumentsAccessor.get(1).toString();
        String password = argumentsAccessor.get(2).toString();
        User newUser = new User(
                name,
                email,
                password
        );
        loginPage.createNewUser(newUser);
        GenericTestHelper.switchWindow(driver);
        Assert.assertThat(driver.getTitle(), is(equalTo("All User")));
        commands.waitForElementVisible(allUserPage.usersTable);
        List<WebElement> tableRows = allUserPage.getUsersTableAsList(driver);
        tableRows.stream()
                .skip(1) //skip table header
                .forEach(
                        row -> {
                            List<String> arrayList = Arrays.asList(row.getText().split("\\s+"));
                            Assertions.assertAll(
                                    () -> Assert.assertThat(arrayList.get(0), is(equalTo(newUser.getName()))),
                                    () -> Assert.assertThat(arrayList.get(1), is(equalTo(newUser.getEmail()))),
                                    () -> Assert.assertThat(arrayList.get(2), is(equalTo(newUser.getPassword())))
                            );
                        }
                );
        Response response = given()
                .get(EndPoints.GETUSERJSON.getResource());
        Assert.assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        User userPj = GenericTestHelper.deserialization(response);
        Assertions.assertAll(
                ()-> Assert.assertThat(userPj.getName(), is(equalTo(newUser.getName()))),
                ()-> Assert.assertThat(userPj.getEmail(), is(equalTo(newUser.getEmail()))),
                ()-> Assert.assertThat(userPj.getPassword(), is(equalTo(newUser.getPassword()))),
                ()-> Assert.assertNotNull(userPj.getId())
        );
    }

    @Epic("LoginPage Functional Test")
    @Feature("Negative: Application should support uniqueness of user")
    @Story("Create two similar user, with identical name and email")
    @Test
    public void testIfSimilarUserCanBeCreatedTwice() {
        User newUser1 = new User(
                "N",
                "d@email.com",
                "123456"
        );
        User newUser2 = new User(
                "N",
                "d@email.com",
                "123456"
        );
        loginPage.createNewUser(newUser1);
        loginPage.createNewUser(newUser2);
        Assertions.assertAll(
                ()-> Assert.assertThat(loginPage.userNameError.getText(), is(equalTo(loginPage.TO_BE_UNIQUE))),
                ()-> Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(loginPage.TO_BE_UNIQUE)))
        );
    }

    @Epic("LoginPage Functional Test")
    @Feature("Positive:Test registration with diff unique credentials")
    @Story("UserPassword within [6;255] range")
    @ParameterizedTest
    @ValueSource(ints = {6, 100, 254, 255})
    public void testIfPasswordWithIntMaxLenghtIsPossible(int number) {
        String randomString = GenericTestHelper.randomString(number, true, true);
        User newUser = new User(
                "Alex" + cnt,
                "alex" + cnt + "@xx.com",
                randomString);
        loginPage.createNewUser(newUser);
        cnt += 1;
    }

    @Epic("LoginPage Functional Test")
    @Feature("User can't be created if password is missed or it breaks password length")
    @Story("UserPassword out of [6;255] range")
    @ParameterizedTest
    @ValueSource(ints = {0, 5, 256})
    public void testIfPasswordSizeOutOfBoundariesIsPossible(int number) {
        String randomString = GenericTestHelper.randomString(number, true, true);
        User newUser = new User(
                "Alex" + cnt,
                "alex" + cnt + "@xx.com",
                randomString);
        loginPage.createNewUser(newUser);
        switch (number) {
            case 0:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.FIELD_IS_REQUIRED)));
                break;
            case 5:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.PASSWORD_ERROR_MIN)));
                break;
            case 256:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(loginPage.PASSWORD_ERROR_MAX)));
                break;
        }
        cnt = +1;
    }

    @AfterAll
    public static void clearDown() {
        driver.close();
    }
}
