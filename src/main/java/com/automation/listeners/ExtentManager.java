package com.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public final class ExtentManager {
    private static ExtentReports extent;

    private ExtentManager() {}

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            new File("reports").mkdirs();
            ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");
            reporter.config().setDocumentTitle("Automation Test Report");
            reporter.config().setReportName("Selenium + TestNG Automation");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Framework", "Selenium + Java + TestNG");
        }
        return extent;
    }
}