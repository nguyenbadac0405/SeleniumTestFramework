package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    private final String filePath;
    private final String sheetName;

    public ExcelUtils(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    private Workbook openWorkbook() throws IOException {
        return new XSSFWorkbook(new FileInputStream(filePath));
    }

    public synchronized String getCellData(int row, int column) throws IOException {
        try (Workbook wb = openWorkbook()) {
            Cell cell = wb.getSheet(sheetName).getRow(row).getCell(column);
            return cell == null ? "" : new DataFormatter().formatCellValue(cell);
        }
    }

    public synchronized List<String> getHeaders() throws IOException {
        try (Workbook wb = openWorkbook()) {
            Row header = wb.getSheet(sheetName).getRow(0);
            List<String> result = new ArrayList<>();
            DataFormatter f = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++)
                result.add(f.formatCellValue(header.getCell(i)).trim());
            return result;
        }
    }

    public synchronized int findRowByTestCaseId(String id) throws IOException {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(sheetName);
            DataFormatter f = new DataFormatter();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row != null && id.equalsIgnoreCase(f.formatCellValue(row.getCell(0)).trim()))
                    return r;
            }
            return -1;
        }
    }

    public synchronized int findColumnByName(String name) throws IOException {
        try (Workbook wb = openWorkbook()) {
            Row header = wb.getSheet(sheetName).getRow(0);
            DataFormatter f = new DataFormatter();
            for (int c = 0; c < header.getLastCellNum(); c++)
                if (name.equalsIgnoreCase(f.formatCellValue(header.getCell(c)).trim())) return c;
            return -1;
        }
    }

    public synchronized void setCellData(String testCaseId, String columnName, String value)
            throws IOException {
        int rowNumber = findRowByTestCaseId(testCaseId);
        int columnNumber = findColumnByName(columnName);
        if (rowNumber < 0) throw new IllegalArgumentException("TestCaseID not found: " + testCaseId);
        if (columnNumber < 0) throw new IllegalArgumentException("Column not found: " + columnName);

        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);
            if (row == null) row = sheet.createRow(rowNumber);
            Cell cell = row.getCell(columnNumber);
            if (cell == null) cell = row.createCell(columnNumber);
            cell.setCellValue(value);
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
            }
        }
    }

    public synchronized int getLastDataRow() throws IOException {
        try (Workbook wb = openWorkbook()) {
            return wb.getSheet(sheetName).getLastRowNum();
        }
    }
}