package com.ds.test.api.ui.pages;

import com.ds.test.api.ui.GenericTestHelper;
import com.ds.test.api.ui.GenericWebSteps;
import com.ds.test.api.ui.PageConstants;
import com.ds.test.api.ui.PageWebElements;
import com.ds.test.api.ui.pojo.User;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage implements PageWebElements {


    private GenericWebSteps commands;
    private WebDriver webDriver;

    public String PASSWORD_ERROR_MIN = "Minimum size is 6";
    public String PASSWORD_ERROR_MAX = "Maximum size is 255";
    public String PASSWORD_REQUIRED = "Required";

    @FindBy(id = "name")
    public WebElement userName;

    @FindBy(id = "email")
    public WebElement userEmail;

    @FindBy(id = "password")
    public WebElement userPassword;

    @FindBy(id = "confirmationPassword")
    public WebElement confirmationPassword;

    @FindBy(xpath = "//a[@href='/users/all']")
    public WebElement allUsersBtn;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement submitBtn;

    @FindBy(id = "registrationForm")
    public WebElement registrationForm;

    @FindBy(id = "user.name.error")
    public WebElement userNameError;

    @FindBy(id = "user.email.error")
    public WebElement userEmailError;

    @FindBy(id = "user.password.error")
    public WebElement userPasswordError;

    @FindBy(id = "user.confirmationPassword.error")
    public WebElement userConfirmationPasswordError;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
        commands = new GenericWebSteps(webDriver);
    }

    public void initialPageLoad() {
        webDriver.get(PageConstants.NEWUSER.getUrl());
        if(commands == null) {
            commands = new GenericWebSteps(this.webDriver);
        } else {
            commands.waitPageLoading(title);
        }
    }

    /**
     * User creation
     */
    private void setName(User user){
        userEmail.sendKeys(user.getName());
    }

    public void createNewUser(User user){
        userName.sendKeys(user.getName());
        userEmail.sendKeys(user.getEmail());
        userPassword.sendKeys(user.getPassword());
        confirmationPassword.sendKeys(user.getPassword());
        GenericTestHelper.isRegistrationFormFilled(
                user, this
        );
        this.submitBtn.click();
    }
}