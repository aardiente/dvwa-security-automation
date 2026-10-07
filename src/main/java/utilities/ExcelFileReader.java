package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ExcelFileReader
{

    public static Object[][] extractData(String fileName, String sheetName)
    {
        Object[][] data = null;

        try (FileInputStream fs = new FileInputStream(fileName);
             Workbook book = new XSSFWorkbook(fs))
        {

            // Default to the first sheet if sheetName is null
            Sheet sheet = (sheetName == null) ? book.getSheetAt(0) : book.getSheet(sheetName);

            if (sheet == null)
            {
                throw new RuntimeException("Sheet not found in " + fileName);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            data = new Object[rowCount][colCount];

            // DataFormatter safely extracts strings, numbers, and handles empty/null cells automatically
            DataFormatter formatter = new DataFormatter();

            // i = 1 to skip the header row
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colCount; j++) {
                        data[i - 1][j] = formatter.formatCellValue(row.getCell(j));
                    }
                } else {
                    // Fill empty rows with blank strings
                    for (int j = 0; j < colCount; j++) {
                        data[i - 1][j] = "";
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read Excel file: " + fileName, e);
        }

        return data;
    }
}