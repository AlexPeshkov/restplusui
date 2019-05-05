package com.ds.tests.firsttorun.smoke;

import com.ds.test.api.TestWebDriver;
import com.ds.ui.pages.AllUserPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class AllUserPageSmokeTests {
    private static WebDriver webDriver;
    private static AllUserPage allUserPage;
    private static TestWebDriver testWebDriver;

    @BeforeAll
    public static void init() {
        testWebDriver = TestWebDriver.newBuilder()
                .setWebDriverManager(CHROME)
                .setWebDriver()
                .build();
        webDriver = testWebDriver.getWebDriver();
        allUserPage = new AllUserPage(webDriver);
        allUserPage.initialPageLoad();
    }

    @Epic("AllUserPage Smoke Test")
    @Feature("If Title == All User")
    @Test
    public void testIfTitleIsEqualToExpectedValue() {
        Assert.assertThat(
                webDriver.getTitle(),
                is(equalTo(allUserPage.getTitle())
                )
        );
        webDriver.close();
    }

    @AfterAll
    public static void close() {
        webDriver.quit();
    }
}