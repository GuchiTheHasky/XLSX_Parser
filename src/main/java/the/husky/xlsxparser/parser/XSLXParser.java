package the.husky.xlsxparser.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.Task;
import the.husky.xlsxparser.web.exception.XSLXParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class XSLXParser {

    @Value("${xslxparser.sheetIndex}")
    private int sheetIndex;

    @Value("${xslxparser.locationRowIndex}")
    private int locationRowIndex;

    @Value("${xslxparser.weightRowIndex}")
    private int weightRowIndex;

    public List<Task> parseXlsxFile(MultipartFile file) {
        List<Task> templateEntities = new ArrayList<>();

        if (!file.isEmpty()) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(sheetIndex);

                int columnCount = sheet.getRow(locationRowIndex).getLastCellNum();

                for (int currentColumn = 1; currentColumn < columnCount; currentColumn++) {

                    Row locationRow = sheet.getRow(locationRowIndex);
                    Row weightRow = sheet.getRow(weightRowIndex);

                    Cell locationCell = locationRow.getCell(currentColumn);
                    Cell weightCell = weightRow.getCell(currentColumn);

                    if (locationCell != null && weightCell != null) {

                        String location = locationCell.getStringCellValue();
                        String address = getAddress(location);
                        String client = getClientName(location);

                        Double weight = weightCell.getNumericCellValue();

                        Task task = createTask(address, client, weight);

                        templateEntities.add(task);
                    }
                }
            } catch (RuntimeException | IOException e) {
                log.error("Error occurred while parsing xlsx file", e.getCause());
                throw new XSLXParserException("Error occurred while parsing xlsx file", e);
            }
        }
        return templateEntities;
    }

        private Task createTask(String address, String client, Double weight) {
           Task task = new Task();
           task.setAddressDetails(address);
           task.setDescription(client);
           task.setDeliveryWeight(weight);
           return task;
        }

    String getClientName(String location) {
        String[] locationParts = location.split(" +");
        if (location.contains("+")) {
            return locationParts[0] + " " + locationParts[1] + " " + locationParts[2] + " " + locationParts[3];
        }
        return locationParts[0] + " " + locationParts[1];
    }

    String getAddress(String location) {
        String clientName = getClientName(location);
        return location.replace(clientName, "").trim();
    }
}
