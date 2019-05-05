package com.ds.test.api;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestWebDriver {

    private WebDriver webDriver;
    private WebDriverManager webDriverManager;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebDriverManager getWebDriverManager() {
        return webDriverManager;
    }

    private TestWebDriver(){};

    public static Builder newBuilder() {
        return new TestWebDriver().new Builder();
    }

    public class Builder {

        public Builder() {}

        public Builder setWebDriver() {
            TestWebDriver.this.webDriver = new ChromeDriver();
            return this;
        }

        public Builder setWebDriverManager(DriverManagerType driver) {
            WebDriverManager.getInstance(driver).setup();
            return this;
        }

        public TestWebDriver build(){
            return TestWebDriver.this;
        }
    }
}
