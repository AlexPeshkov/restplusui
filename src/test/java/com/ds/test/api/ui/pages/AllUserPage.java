package com.ds.test.api.ui.pages;

import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.PageWebElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AllUserPage implements PageWebElements {

    private GenericWebSteps commands;
    WebDriver webDriver;

    @FindBy(xpath = "//a[@href='/user/new']")
    public WebElement newUserBtn;

    public AllUserPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
        commands = new GenericWebSteps(webDriver);
    }

    public void initialPageLoad() {
        webDriver.get(PageConstants.ALLUSERS.getUrl());
        if(commands == null) {
            commands = new GenericWebSteps(this.webDriver);
        } else {
            commands.waitPageLoading(title);
        }
    }



}
