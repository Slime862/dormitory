package com.shixin.other.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class ExportExcelUtil {

    /**
     * 下载
     *
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @param workbook Workbook
     */
    public static void downLoadExcel(HttpServletResponse response, String fileName, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置sum公式
     *
     * @param wk      Workbook
     * @param sumCol  合计
     * @param itemCol 合计的项
     */
    public static void setSumFormula(Workbook wk, Character sumCol, Character... itemCol) {
        StringBuffer buffer = new StringBuffer();
        Sheet sheet = wk.getSheetAt(0);
        Row row = null;
        for (int i = sheet.getFirstRowNum() + 2; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Character item : itemCol) {
                CellAddress address = row.createCell(colCharToNum(item)).getAddress();
                buffer.append(address + ":");
            }

            String substring = buffer.toString().substring(0, buffer.toString().lastIndexOf(":"));
            row.createCell(colCharToNum(sumCol)).setCellFormula(String.format("sum(%s)", substring));
            buffer.setLength(0);
        }
    }

    /**
     * 列英文标识转数字列号
     *
     * @param character char
     * @return
     */
    private static Integer colCharToNum(Character character) {
        return (int) character - 65;
    }
}
