package com.ds.test.api.ui.tests.smoke;

import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.pages.AllUserPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class AllUserPageSmokeTests {
    private static WebDriver driver;
    private static AllUserPage allUserPage;

    @Test
    public void testIfTitleIsEqualToExpectedValue(){
        WebDriverManager.getInstance(CHROME).setup();
        driver = new ChromeDriver();
        allUserPage = new AllUserPage(driver);
        allUserPage.initialPageLoad();
        Assert.assertThat(driver.getTitle(), is(equalTo(PageConstants.ALLUSERS.getTitle())));
        driver.close();
    }
}
