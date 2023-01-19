package com.jimm0063.magi.api.control.deudas.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDate;

public class ExcelBuilder {
    private Workbook workbook;
    private Integer currentRow = -1;

    public ExcelBuilder createWorkbook() {
        workbook = new HSSFWorkbook();
        return this;
    }

    public Sheet createSheet() throws Exception {
        if(this.workbook == null) throw new Exception("There is no book to add a new sheet");
        return this.workbook.createSheet();
    }

    public Row addRow(Sheet sheet) throws Exception {
        if(currentRow == -1) currentRow = 0;
        currentRow++;
        return sheet.createRow(currentRow);
    }

    public void writeStringCell(Row row, Integer cellIndex,  String data) {
        row.createCell(cellIndex).setCellValue(data);
    }

    public void writeDoubleCell(Row row, Integer cellIndex,  Double data) {
        row.createCell(cellIndex).setCellValue(data);
    }

    public void writeDateCell(Row row, Integer cellIndex,  LocalDate data) {
        row.createCell(cellIndex).setCellValue(data);
    }

    public Workbook getExcelFile() {
        return this.workbook;
    }
}
