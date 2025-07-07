package com.struts.services;

import com.struts.dao.UserDao;
import com.struts.models.User;
import com.struts.util.CommonUtil;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadService {

    private final UserDao userDao = new UserDao();

    private static final Logger logger = LoggerUtil.getLogger(DownloadService.class);

    /*public static void main(String[] args) {
        DownloadService service = new DownloadService();
        String filePath = "D:\\RAHUL\\Eclipse\\Intellij\\RegistrationStruts\\module-resource\\All-Users.xlsx";
        service.addUsersDataInFile(filePath);
    }*/

    public String updateFileData(Class<?> clazz) {
        Map<String, Object> result = new HashMap<>();
        try {
            logger.debug("className :: [{}]", clazz.getSimpleName());
            if ("User".equals(clazz.getSimpleName())) {
                String filePath = "D:\\RAHUL\\Eclipse\\Intellij\\RegistrationStruts\\module-resource\\All-users-details.xlsx";
                addUsersDataInFile(filePath);
                return filePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unsupported class: " + clazz.getSimpleName());
    }

    private void addUsersDataInFile(String filePath) {
        try {
            List<User> allUsers = userDao.getAllUser();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet testSheet = workbook.createSheet("users");

            // Create title in row 0
            createTitleRow(workbook, testSheet, "All Users Details", 6);

            // Create header in row 1
            String[] headers = {"User ID", "First Name", "Last Name", "Email Id", "DOB", "Gender"};
            createHeaderRow(workbook, testSheet, headers);

            CellStyle dateStyle = workbook.createCellStyle();
            short dateFormat = workbook.createDataFormat().getFormat("dd/MM/yyyy");
            dateStyle.setDataFormat(dateFormat);

            CellStyle oddRowStyle = workbook.createCellStyle();
            oddRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            //oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle evenRowStyle = workbook.createCellStyle();
            evenRowStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            //evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowNum = 1;
            for (User user : allUsers) {
                ++rowNum;   // Starts from row 2 (third row in sheet)
                CellStyle dataStyle = workbook.createCellStyle();

                if(rowNum % 2 == 0){
                    dataStyle.cloneStyleFrom(evenRowStyle);
                    dateStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
                } else {
                    dateStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    dataStyle.cloneStyleFrom(oddRowStyle);
                }

                Row row = testSheet.createRow(rowNum);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(user.getUserId());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(user.getFirstName());
                cell1.setCellStyle(dataStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(user.getLastName());
                cell2.setCellStyle(dataStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(user.getEmail());
                cell3.setCellStyle(dataStyle);

                // LocalDate to java.util.Date
                LocalDate dobLocalDate = user.getDob();
                Date dobDate = java.sql.Date.valueOf(dobLocalDate);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(dobDate);  // Write Date
                cell4.setCellStyle(dateStyle);  // set Date style

                // determine gender value based on initial
                String gender = (user.getGender() == 'M') ? "Male" : (user.getGender() == 'F') ? "Female" : "Other";
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(gender);
                cell5.setCellStyle(dataStyle);
            }

            // to set column size to auto
            for (int i = 0; i < headers.length; i++) {
                testSheet.autoSizeColumn(i);
            }

            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to create the title row
    public static void createTitleRow(Workbook workbook, Sheet sheet, String title, int columnSpan) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.WHITE.getIndex());

        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // Background color
        titleStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        // Merge columns (0 to columnSpan - 1)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSpan - 1));
    }

    // Method to create the header row
    public static void createHeaderRow(Workbook workbook, Sheet sheet, String[] headers) {
        CellStyle headerStyle = workbook.createCellStyle();
        // Bold font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        // Background color
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        headerStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());

        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
    }

    /*public static void main(String[] args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet testSheet = workbook.createSheet("test");
        testSheet.createRow(0);
        testSheet.getRow(0).createCell(0).setCellValue("User ID");
        testSheet.getRow(0).createCell(1).setCellValue("First Name");
        testSheet.getRow(0).createCell(2).setCellValue("Last Name");
        testSheet.getRow(0).createCell(3).setCellValue("Email Id");
        testSheet.getRow(0).createCell(4).setCellValue("DOB");
        testSheet.getRow(0).createCell(5).setCellValue("Gender");

        testSheet.createRow(1);
        testSheet.getRow(1).createCell(0).setCellValue(18);
        testSheet.getRow(1).createCell(1).setCellValue("John");
        testSheet.getRow(1).createCell(2).setCellValue("Doe");
        testSheet.getRow(1).createCell(3).setCellValue("johndoe@gmail.com");
        testSheet.getRow(1).createCell(4).setCellValue("11/05/2000");
        testSheet.getRow(1).createCell(5).setCellValue("Male");

        File file = new File("D:\\RAHUL\\Eclipse\\Intellij\\RegistrationStruts\\module-resource\\test.xlsx");
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
    }*/
}
