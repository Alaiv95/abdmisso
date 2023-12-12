package workers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.IssoData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReader {
    private final Workbook workbook;
    private final CellStyle defaultStyle;
    private final Sheet sheet;
    private Row row;

    int currentRowNumber = 1;

    public ExcelReader(Workbook workbook) {
        this.workbook = workbook;
        sheet = workbook.createSheet("IssoObjects");

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);

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
        initCell(headerStyle, 1, "ФКУ");
        initCell(headerStyle, 2, "Дорога");
        initCell(headerStyle, 3, "Код дороги АБДМ");
        initCell(headerStyle, 4, "КМ");
        initCell(headerStyle, 5, "М");


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
        initCell(basedStyle, 1, issoData.getOrgName());
        initCell(basedStyle, 2, issoData.getDorName());
        initCell(basedStyle, 3, issoData.getDorCode());
        initCell(basedStyle, 4, String.valueOf(issoData.getKm()));
        initCell(basedStyle, 5, String.valueOf(issoData.getM()));

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
