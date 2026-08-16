package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By error = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public LoginPage enterUsername(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(value);
        return this;
    }

    public LoginPage enterPassword(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(value);
        return this;
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void login(String user, String pass) {
        enterUsername(user).enterPassword(pass).clickLogin();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }

    public boolean isLoginSuccessful() {
        return !driver.findElements(By.id("inventory_container")).isEmpty();
    }
}