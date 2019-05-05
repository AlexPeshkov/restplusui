package com.ds.tests.firsttorun.smoke;

import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.TestWebDriver;
import com.ds.ui.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class LoginPageSmokeTests {
    private static WebDriver driver;
    private static LoginPage loginPage;
    private static GenericWebSteps commands;
    private static TestWebDriver testWebDriver;

    @BeforeAll
    public static void initWebDriver() {
        testWebDriver = TestWebDriver.newBuilder()
                .setWebDriverManager(CHROME)
                .setWebDriver()
                .build();
        driver = testWebDriver.getWebDriver();
        loginPage = new LoginPage(driver);
        commands = new GenericWebSteps(driver);
        loginPage.initialPageLoad();
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If Title == New User")
    @Test
    public void testIfTitleIsEqualToExpectedValue() {
        Assert.assertThat(driver.getTitle(), is(equalTo(loginPage.getTitle())));
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If UserName element is visible")
    @Test
    public void testIfUerNameElementIsVisible() {
        commands.waitForElementVisible(loginPage.userName);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If UserEmail element is visible")
    @Test
    public void testIfUserEmailElementIsVisible() {
        commands.waitForElementVisible(loginPage.userEmail);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If UserPassword element is visible")
    @Test
    public void testIfUserPasswordElementIsVisible() {
        commands.waitForElementVisible(loginPage.userPassword);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If 'Submit' button is visible")
    @Test
    public void testIfSubmitBtnElementIsVisible() {
        commands.waitForElementVisible(loginPage.submitBtn);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If 'All User' button is visible")
    @Test
    public void testIfAllUsersBtnElementIsVisible() {
        commands.waitForElementVisible(loginPage.allUsersBtn);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If PasswordConfirmation element is visible")
    @Test
    public void testIfConfirmationPasswordElementIsVisible() {
        commands.waitForElementVisible(loginPage.confirmationPassword);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If registration form is visible")
    @Test
    public void testIfRegistrationFormElementIsVisible() {
        commands.waitForElementVisible(loginPage.registrationForm);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If 'Submit' button is clickable")
    @Test
    public void testIfSubmitBtnIsClickable() {
        commands.waitForElementClickable(loginPage.submitBtn);
    }

    @Epic("LoginPage Smoke Test")
    @Feature("If 'All USer' button is clickable")
    @Test
    public void testIfAllUsersBtnIsClickable() {
        commands.waitForElementClickable(loginPage.allUsersBtn);
    }

    @AfterAll
    public static void clearDown() {
        driver.close();
    }
}
