package the.husky.xlsxparser.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.TemplateInfo;
import the.husky.xlsxparser.web.exception.XSLXParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class XSLXParser {

    public List<TemplateInfo> parseXlsxFile(MultipartFile file) {
        List<TemplateInfo> templateEntities = new ArrayList<>();

        if (!file.isEmpty()) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
                 Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                int locationRowIndex = 1;
                int weightRowIndex = 25;

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

                        TemplateInfo templateInfo = craeteTemplateInfo(address, client, weight);

                        templateEntities.add(templateInfo);
                    }
                }
            } catch (IOException e) {
                throw new XSLXParserException("Error occurred while parsing xlsx file", e.getCause());
            }
        }
        return templateEntities;
    }

    private TemplateInfo craeteTemplateInfo(String address, String client, Double weight) {
        return TemplateInfo.builder()
                .address(address)
                .client(client)
                .weight(weight)
                .build();
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
