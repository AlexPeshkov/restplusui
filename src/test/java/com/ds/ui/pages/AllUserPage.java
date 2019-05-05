package com.ds.ui.pages;

import com.ds.test.api.rest.BasePath;
import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.rest.EndPoints;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AllUserPage extends AbstractPage {

    private GenericWebSteps commands;
    private final String TITLE = "All User";
    WebDriver webDriver;

    @FindBy(xpath = "//a[@href='/user/new']")
    public WebElement newUserBtn;

    /**
     * Users table
     *
     * @param webDriver
     */
    @FindBy(id = "users")
    public WebElement usersTable;

    public String getTitle() {
        return TITLE;
    }

    public AllUserPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
        commands = new GenericWebSteps(webDriver);
    }

    @Override
    public void initialPageLoad() {
        webDriver.get(BasePath.BASEURL.getResource().concat(EndPoints.USERSRALL.getResource()));
        if (commands == null) {
            commands = new GenericWebSteps(this.webDriver);
        } else {
            commands.waitPageLoading(title);
        }
    }

    public List<WebElement> getUsersTableAsList(WebDriver webDriver) {
        return new WebDriverWait(webDriver, 10).until(
                ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.usersTable, By.tagName("tr")));
    }
}