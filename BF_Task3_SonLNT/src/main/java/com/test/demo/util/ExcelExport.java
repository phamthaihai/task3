package com.test.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/** Builds single-sheet .xlsx downloads with a bold header row. */
public final class ExcelExport {

	private static final MediaType XLSX =
			MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	private ExcelExport() {
	}

	public static ResponseEntity<byte[]> download(String filename, String sheetName, List<String> headers,
			List<List<String>> rows) {
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(sheetName);
			Font bold = workbook.createFont();
			bold.setBold(true);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(bold);

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.size(); i++) {
				headerRow.createCell(i).setCellValue(headers.get(i));
				headerRow.getCell(i).setCellStyle(headerStyle);
			}
			for (int r = 0; r < rows.size(); r++) {
				Row row = sheet.createRow(r + 1);
				List<String> values = rows.get(r);
				for (int i = 0; i < values.size(); i++) {
					row.createCell(i).setCellValue(values.get(i));
				}
			}
			for (int i = 0; i < headers.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			workbook.write(out);

			return ResponseEntity.ok()
					.contentType(XLSX)
					.header(HttpHeaders.CONTENT_DISPOSITION,
							ContentDisposition.attachment().filename(filename).build().toString())
					.body(out.toByteArray());
		}
		catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

}
