package edu.cs4730.qrDemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class Excel2SQLiteHelper {
	/**
	 * 读�?�Excel文档中的一张表�?�，用ContentValues�?装，然�?��?�入数�?�库中
	 * 
	 * @param sheet
	 */
	public static void insertExcelToSqlite(DBAdapter dbAdapter, Sheet sheet) {
		for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
			Row row = rit.next();
			ContentValues values = new ContentValues();
			values.put(DBAdapter.STU_NAME, row.getCell(0).getNumericCellValue());
			values.put(DBAdapter.STU_AGE, row.getCell(1).getStringCellValue());
			values.put(DBAdapter.STU_SCORE, row.getCell(2).getStringCellValue());
			
			values.put(DBAdapter.STU_BIRTH, row.getCell(3).getStringCellValue());
			if (dbAdapter.insert(DBAdapter.STU_TABLE, values) < 0) {
				Log.e("Error", "�?�入数�?�错误");
				return;
			}
		}
	}

	/**
	 * 使用POI创建Excel示例，表格数�?�和Sqlite中的�?对应
	 */
	public static void createExcel(Context context) {
		Workbook workbook = new HSSFWorkbook();
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);// 表格内容�?�左对�?
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
		// 姓�??，年龄，生日
		Object[] values0 = { "TangYC", 70, 20, new Date() };
		Object[] values1 = { "Eric", 71, 21, new Date() };
		Object[] values2 = { "Tomas", 72, 22, new Date() };
		Object[] values3 = { "Tonny", 73, 23, new Date() };
		Object[] values4 = { "Jimmy", 74, 24, new Date() };
		// 创建第一张表�?�
		Sheet sheet1 = workbook.createSheet("class1");
		insertRow(sheet1, 0, values0, cellStyle);
		insertRow(sheet1, 1, values1, cellStyle);
		insertRow(sheet1, 2, values2, cellStyle);
		insertRow(sheet1, 3, values3, cellStyle);
		insertRow(sheet1, 4, values4, cellStyle);
		// 创建第二张表�?�
		Object[] values5 = { "Aron", 81, 25, Calendar.getInstance() };
		Object[] values6 = { "Truman", 82, 26, Calendar.getInstance() };
		Object[] values7 = { "T-bag", 83, 27, Calendar.getInstance() };
		Object[] values8 = { "WhyMe", 84, 28, Calendar.getInstance() };
		Object[] values9 = { "Youknowit", 85, 29, Calendar.getInstance() };
		Sheet sheet2 = workbook.createSheet("class2");

		insertRow(sheet2, 0, values5, cellStyle);
		insertRow(sheet2, 1, values6, cellStyle);
		insertRow(sheet2, 2, values7, cellStyle);
		insertRow(sheet2, 3, values8, cellStyle);
		insertRow(sheet2, 4, values9, cellStyle);

		// �?存文档
		File file = new File("/sdcard/students.xls");
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �?�入一行数�?�
	 * 
	 * @param sheet
	 *            �?�入数�?�行的表�?�
	 * @param rowIndex
	 *            �?�入的行的索引
	 * @param columnValues
	 *            �?�?�入一行中的数�?�，数组表示
	 * @param cellStyle
	 *            该格中数�?�的显示样�?
	 */
	public static void insertRow(Sheet sheet, int rowIndex,
			Object[] columnValues, CellStyle cellStyle) {
		Row row = sheet.createRow(rowIndex);
		int column = columnValues.length;
		for (int i = 0; i < column; i++) {
			createCell(row, i, columnValues[i], cellStyle);
		}
	}

	/**
	 * 在一行中�?�入一个�?�元值
	 * 
	 * @param row
	 *            �?�?�入的数�?�的行
	 * @param columnIndex
	 *            �?�入的列的索引
	 * @param cellValue
	 *            该cell的值：如果是Calendar或者Date类型，就先对其格�?化
	 * @param cellStyle
	 *            该格中数�?�的显示样�?
	 */
	public static void createCell(Row row, int columnIndex, Object cellValue,
			CellStyle cellStyle) {
		Cell cell = row.createCell(columnIndex);
		// 如果是Calender或者Date类型的数�?�，就格�?化�?字符串
		if (cellValue instanceof Date) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String value = format.format((Date) cellValue);
			cell.setCellValue(value);
		} else if (cellValue instanceof Calendar) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String value = format.format(((Calendar) cellValue).getTime());
			cell.setCellValue(value);
		} else {
			cell.setCellValue(cellValue.toString());
		}
		cell.setCellStyle(cellStyle);
	}

}
