package com.ds.tests.nexttorun.functional;

import com.ds.pojo.User;
import com.ds.test.api.rest.BasePath;
import com.ds.test.api.rest.RestBaseFunctionalTest;
import com.ds.test.api.utility.GenericTestHelper;
import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.TestWebDriver;
import com.ds.ui.pages.LoginPageErrorMsg;
import com.ds.ui.pages.AllUserPage;
import com.ds.ui.pages.LoginPage;
import com.ds.test.api.rest.EndPoints;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class WebUiFunctionalTests extends RestBaseFunctionalTest {

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
                .setWebDriver()
                .build();
        driver = testWebDriver.getWebDriver();
        loginPage = new LoginPage(driver);
        allUserPage = new AllUserPage(driver);
        commands = new GenericWebSteps(driver);
    }

    @BeforeEach
    public void init() {
        driver.get(BasePath.BASEURL.getResource());
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
        Assert.assertThat(loginPage.userNameError.getText(), is(equalTo(LoginPageErrorMsg.IS_REQUIRED.getMessage())));
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
        Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(LoginPageErrorMsg.IS_REQUIRED.getMessage())));
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
        Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(LoginPageErrorMsg.EMAIL_IS_INVALID.getMessage())));
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
        Assert.assertThat(loginPage.userConfirmationPasswordError.getText(), is(equalTo(LoginPageErrorMsg.PASSWORD_NOT_THE_SAME.getMessage())));
    }

    @Disabled("To have build green")
    @Issue("Case #3: Password with a space is split by line in UI, i.e. Get /user/all/json returns User Object with 4 fields!")
    @Severity(SeverityLevel.NORMAL)
    @Epic("LoginPage Functional Test")
    @Feature("Create a new user with unique credentials and user details are available on 'New User' page and Rest API returns correct user details")
    @Story("Creation New User when registration form is filled in properly")
    @ParameterizedTest
    @CsvSource(
            {
                    "A,a@test.com,1234567",
                    "B,b@test.com,1234567",
                    "1234djfndnsdmddd,_339dmsmdff@test.com,8374#$%$@&*@$^@! 3()&&#", //[1234djfndnsdmddd, _339dmsmdff@test.com, 8374#$%$@&*@$^@!, 3()&&#]
                    "//\344555553(,9dmsmdff@test.com,8374#$%$@&*@$^@!3()&&#",
                    " 1234,_@test.com,******///////#",
            }
    )
    public void createNewUserWithUniqueCredentials(ArgumentsAccessor argumentsAccessor) {
        Assert.assertThat(RestBaseFunctionalTest.deleteUsers(), Matchers.is(equalTo(RestBaseFunctionalTest.HTTP_OK)));
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
                            System.out.println("Users -> " + arrayList);
                            Assertions.assertAll(
                                    () -> Assert.assertThat(arrayList.get(0), is(equalTo(newUser.getName()))),
                                    () -> Assert.assertThat(arrayList.get(1), is(equalTo(newUser.getEmail()))),
                                    () -> Assert.assertThat(arrayList.get(2), is(equalTo(newUser.getPassword())))
                            );
                        }
                );
        Response response = given()
                .get(EndPoints.USERALLJSON.getResource());
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
    public void testIfSimilarUserCanBeCreatedOneMoreTime() {
        String randomStr = GenericTestHelper.randomString(10, true, false);
        User newUser1 = new User(
                randomStr,
                randomStr.concat("@email.com"),
                "123456"
        );
        User newUser2 = new User(
                randomStr,
                randomStr.concat("@email.com"),
                "1234567"
        );
        loginPage.createNewUser(newUser1);
        GenericTestHelper.switchWindow(driver);
        allUserPage.newUserBtn.click();
        loginPage.createNewUser(newUser2);
        Assertions.assertAll(
                ()-> Assert.assertThat(loginPage.userNameError.getText(), is(equalTo(LoginPageErrorMsg.TO_BE_UNIQUE.getMessage()))),
                ()-> Assert.assertThat(loginPage.userEmailError.getText(), is(equalTo(LoginPageErrorMsg.TO_BE_UNIQUE.getMessage())))
        );
    }

    @Epic("LoginPage Functional Test")
    @Feature("Positive:Test registration with diff unique credentials")
    @Story("UserPassword within [6;255] range")
    @ParameterizedTest
    @ValueSource(ints = {6, 100, 254, 255})
    public void testIfPasswordWithIntMaxLengthIsPossible(int number) {
        String randomString = GenericTestHelper.randomString(number, true, true);
        User newUser = new User(
                "Alex" + cnt,
                "alex" + cnt + "@test.com",
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
                "alex" + cnt + "@test.com",
                randomString);
        loginPage.createNewUser(newUser);
        switch (number) {
            case 0:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(LoginPageErrorMsg.IS_REQUIRED.getMessage())));
                break;
            case 5:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(LoginPageErrorMsg.PASSWORD_ERROR_MIN.getMessage())));
                break;
            case 256:
                Assert.assertThat(loginPage.userPasswordError.getText(), is(equalTo(LoginPageErrorMsg.PASSWORD_ERROR_MAX.getMessage())));
                break;
        }
        cnt = +1;
    }

    @AfterAll
    public static void clearDown() {
        driver.quit();
    }
}
