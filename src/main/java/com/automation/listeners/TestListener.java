package com.automation.listeners;

import com.automation.utils.LoggerUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TestListener implements ITestListener {
    private static final org.slf4j.Logger log = LoggerUtils.getLogger(TestListener.class);
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getName();
        TEST.set(ExtentManager.getExtent().createTest(name));
        log.info("START: {}", name);
        TEST.get().log(Status.INFO, "Test started: " + name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("PASS: {}", result.getName());
        TEST.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("FAIL: {}", result.getName(), result.getThrowable());
        TEST.get().fail(result.getThrowable());

        Object instance = result.getInstance();
        if (instance instanceof com.automation.base.BaseTest base) {
            WebDriver driver = base.driver;
            if (driver != null) {
                String path = takeScreenshot(driver, result.getName());
                try {
                    TEST.get().addScreenCaptureFromPath(path);
                } catch (Exception e) {
                    log.warn("Could not attach screenshot", e);
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("SKIP: {}", result.getName());
        TEST.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getExtent().flush();
    }

    private String takeScreenshot(WebDriver driver, String testName) {
        try {
            File dir = new File("reports/screenshots");
            dir.mkdirs();
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path target = Path.of("reports/screenshots", safeName + "_" + System.currentTimeMillis() + ".png");
            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Screenshot: {}", target);
            return target.toString();
        } catch (Exception e) {
            log.error("Screenshot failed", e);
            return "";
        }
    }
}