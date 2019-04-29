package com.ds.test.api.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class GenericWebSteps {

    private WebDriver webDriver;
    private FluentWait fluentWait;

    public GenericWebSteps(WebDriver webDriver) {
        this.webDriver = webDriver;
        fluentWait = new FluentWait(webDriver)
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class);
    }

    public void waitForElementVisible(By locator) {
        fluentWait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public void waitForElementVisible(WebElement element) {
        fluentWait.until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    public void waitPageLoading(By locator){
        waitForElementVisible(locator);
    }

    public void waitForElementUntilIsPresent(By locator){
        fluentWait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    public void waitForElementClickable(WebElement element) {
        fluentWait.until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }
}
