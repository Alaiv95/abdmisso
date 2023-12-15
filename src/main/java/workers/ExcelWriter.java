package workers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import models.IssoData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {
    private final Workbook workbook;
    private final CellStyle defaultStyle;
    private final Sheet sheet;
    private Row row;

    int currentRowNumber = 1;

    public ExcelWriter(Workbook workbook) {
        this.workbook = workbook;
        sheet = workbook.createSheet("IssoObjects");

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 7000);
        sheet.setColumnWidth(5, 10000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 7000);
        sheet.setColumnWidth(9, 11000);

        row = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        initCell(headerStyle, 0, "Код иссо");
        initCell(headerStyle, 1, "Тип иссо");
        initCell(headerStyle, 2, "ФКУ");
        initCell(headerStyle, 3, "Дорога");
        initCell(headerStyle, 4, "Код дороги АБДМ");
        initCell(headerStyle, 5, "Id дорог АБДД");
        initCell(headerStyle, 6, "КМ");
        initCell(headerStyle, 7, "М");
        initCell(headerStyle, 8, "Длина сооружения");
        initCell(headerStyle, 9, "Пересечения с дорогами АБДД");


        defaultStyle = workbook.createCellStyle();
        defaultStyle.setWrapText(true);
    }

    public void saveExcelFile(String fileName) throws IOException {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + fileName +".xlsx";

        try(FileOutputStream outputStream = new FileOutputStream(fileLocation);) {
            workbook.write(outputStream);
            workbook.close();
        }
    }
    public void addRow(IssoData issoData, boolean isEmpty) {
        CellStyle basedStyle = workbook.createCellStyle();
        chooseCellColor(basedStyle, isEmpty);
        basedStyle.setWrapText(true);
        basedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        drawCellBorders(basedStyle);

        row = sheet.createRow(currentRowNumber);

        initCell(basedStyle, 0, String.valueOf(issoData.getCIsso()));
        initCell(basedStyle, 1, String.valueOf(issoData.getIssoTypeCode()));
        initCell(basedStyle, 2, issoData.getOrgName());
        initCell(basedStyle, 3, issoData.getDorName());
        initCell(basedStyle, 4, issoData.getDorCode());
        initCell(basedStyle, 5, issoData.getAbddIds());
        initCell(basedStyle, 6, String.valueOf(issoData.getKm()));
        initCell(basedStyle, 7, String.valueOf(issoData.getM()));
        initCell(basedStyle, 8, String.valueOf(issoData.getLength()));
        initCell(basedStyle, 9, String.valueOf(issoData.getRoadsWithMatchingLen()));

        currentRowNumber++;
    }


    private void drawCellBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }

    private void initCell(CellStyle headerStyle, int cellNum, String val) {
        Cell headerCell = row.createCell(cellNum);
        headerCell.setCellValue(val);
        headerCell.setCellStyle(headerStyle);
    }

    private void chooseCellColor(CellStyle style, boolean isEmpty) {
        if (!isEmpty) {
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        } else {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
        }
    }

}
