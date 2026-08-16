package com.automation.dataprovider;

import com.automation.config.ConfigReader;
import com.automation.utils.ExcelUtils;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginDataProvider {
    @DataProvider(name = "loginData")
    public static Object[][] loginData() throws IOException {
        ExcelUtils excel = new ExcelUtils(
                ConfigReader.get("excelPath", "testdata/LoginData.xlsx"),
                ConfigReader.get("excelSheet", "LoginTest"));

        List<Object[]> rows = new ArrayList<>();
        int lastRow = excel.getLastDataRow();

        for (int r = 1; r <= lastRow; r++) {
            String id = excel.getCellData(r, 0).trim();
            if (id.isEmpty()) continue;

            String username = excel.getCellData(r, 1);
            String password = excel.getCellData(r, 2);
            String expected = excel.getCellData(r, 3);
            rows.add(new Object[]{id, username, password, expected});
        }
        return rows.toArray(new Object[0][]);
    }
}