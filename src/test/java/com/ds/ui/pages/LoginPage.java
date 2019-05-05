package com.ds.ui.pages;

import com.ds.pojo.User;
import com.ds.test.api.rest.BasePath;
import com.ds.test.api.utility.GenericTestHelper;
import com.ds.test.api.ui.GenericWebSteps;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractPage {

    private GenericWebSteps commands;
    private WebDriver webDriver;
    private final String TITLE = "New User";

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

    public String getTitle() {
        return TITLE;
    }

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
        commands = new GenericWebSteps(webDriver);
    }

    @Override
    public void initialPageLoad() {
        webDriver.get(BasePath.BASEURL.getResource());
        if (commands == null) {
            commands = new GenericWebSteps(this.webDriver);
        } else {
            commands.waitPageLoading(title);
        }
    }

    /**
     * User creation
     */
    private void setName(User user) {
        userEmail.sendKeys(user.getName());
    }

    public void createNewUser(User user) {
        userName.sendKeys(user.getName());
        userEmail.sendKeys(user.getEmail());
        userPassword.sendKeys(user.getPassword());
        confirmationPassword.sendKeys(user.getPassword());
        GenericTestHelper.isRegistrationFormFilled(
                user, this
        );
        this.submitBtn.click();
    }

    public void createNewUser(User user, String passwordRepeat) {
        userName.sendKeys(user.getName());
        userEmail.sendKeys(user.getEmail());
        userPassword.sendKeys(user.getPassword());
        confirmationPassword.sendKeys(passwordRepeat);
        GenericTestHelper.isRegistrationFormFilled(
                user, this
        );
        this.submitBtn.click();
    }
}