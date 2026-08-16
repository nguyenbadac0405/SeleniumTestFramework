package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.dataprovider.LoginDataProvider;
import com.automation.pages.LoginPage;
import com.automation.utils.ExcelUtils;
import com.automation.utils.LoggerUtils;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest extends BaseTest {
    private static final org.slf4j.Logger log = LoggerUtils.getLogger(LoginTest.class);

    private final ExcelUtils excel = new ExcelUtils(
            ConfigReader.get("excelPath", "testdata/LoginData.xlsx"),
            ConfigReader.get("excelSheet", "LoginTest"));

    @Test(dataProvider = "loginData", dataProviderClass = LoginDataProvider.class)
    public void loginTest(String testCaseId, String username, String password, String expected) {
        log.info("Running {}", testCaseId);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        if ("Login success".equalsIgnoreCase(expected)) {
            Assert.assertTrue(loginPage.isLoginSuccessful(),
                    "Expected successful login for " + testCaseId);
        } else {
            String actualError = loginPage.getErrorMessage();
            Assert.assertTrue(!actualError.isBlank(),
                    "Expected login error for " + testCaseId);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void updateExcelResult(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length == 0) return;

        String testCaseId = String.valueOf(parameters[0]);
        String status = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASS";
            case ITestResult.FAILURE -> "FAIL";
            default -> "SKIP";
        };

        try {
            excel.setCellData(testCaseId, "Status", status);
            log.info("{} => {}", testCaseId, status);
        } catch (IOException e) {
            log.error("Cannot update Excel for " + testCaseId, e);
        }
    }
}