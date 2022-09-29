package com.example.demo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ImportService {

    public List getBankListByExcel(InputStream in, String fileName) throws Exception {
        List list = new ArrayList<>();

        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("Create Excel sheet is null！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = (Sheet) work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = ((org.apache.poi.ss.usermodel.Sheet) sheet).getFirstRowNum(); j <= ((org.apache.poi.ss.usermodel.Sheet) sheet).getLastRowNum(); j++) {
                row = ((org.apache.poi.ss.usermodel.Sheet) sheet).getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(cell);
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("please upload excel file!");
        }
        return workbook;
    }

}
