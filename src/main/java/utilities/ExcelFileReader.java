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

public class ExcelFileReader {

    public static Object[][] extractData(String fileName, String sheetName) {
        Object[][] data = null;

        // 1. Strip away any accidental folder paths passed in the string (e.g., "src/test/.../Logins.xlsx" becomes "Logins.xlsx")
        String cleanFileName = new File(fileName).getName();

        // 2. Get the root directory of your project
        String projectDir = System.getProperty("user.dir");
        File targetFile = null;

        // 3. Dynamically scan the project folder to find the file wherever it lives
        try (Stream<Path> paths = Files.walk(Paths.get(projectDir))) {
            targetFile = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().equals(cleanFileName))
                    .map(Path::toFile)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.out.println("Error scanning project directory.");
        }

        // 4. Fail if the file genuinely does not exist in the project
        if (targetFile == null) {
            throw new RuntimeException("FileNotFound: Could not find '" + cleanFileName + "' anywhere inside " + projectDir);
        }

        System.out.println("Found test data file at: " + targetFile.getAbsolutePath());

        // 5. Read the Excel File
        try (FileInputStream fs = new FileInputStream(targetFile)) {
            Workbook book = new XSSFWorkbook(fs);
            Sheet sheet = book.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' does not exist in " + cleanFileName);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            data = new Object[rowCount][colCount];

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    for (int j = 0; j < colCount; j++) {
                        Cell cell = row.getCell(j);

                        if (cell == null) {
                            data[i - 1][j] = "";
                        } else {
                            data[i - 1][j] = cell.getStringCellValue();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}