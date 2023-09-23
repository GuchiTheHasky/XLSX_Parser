package the.husky.xlsxparser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.Task;
import the.husky.xlsxparser.web.exception.XSLXParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("XSLXParser unit tests")
@TestPropertySource(locations = "classpath:test_app.properties")
class XSLXParserTest {
    private final static String LOCATION_SCHOOL =
            "280 школа Євгенія Харченка вул., 23Б, Євгенія Харченка вул., 23Б";
    private final static String LOCATION_GYMNASIUM =
            "Київська Русь Гімназія Бориса Гмирі вул., 2В, Бориса Гмирі вул., 2В";

    @Autowired private XSLXParser xslxParser;

    @Test
    @DisplayName("Test, get address.")
    public void testGetAddress() {
        String expected = "Євгенія Харченка вул., 23Б, Євгенія Харченка вул., 23Б";
        String actual = xslxParser.getAddress(LOCATION_SCHOOL);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test, get client name.")
    public void testGetClientName() {
        String expected = "280 школа";
        String actual = xslxParser.getClientName(LOCATION_SCHOOL);

        assertEquals(expected, actual);

        String expected2 = "Київська Русь";
        String actual2 = xslxParser.getClientName(LOCATION_GYMNASIUM);

        assertEquals(expected2, actual2);
    }

    @Test
    @DisplayName("Test, should parse xlsx file, check size and json.")
    public void testParseXSLXFileCheckSizeAndContent() throws IOException {
        String fileName = "test_template.xlsx";
        byte[] fileContent = getFileContent(fileName); 

        MultipartFile multipartFile = getMultipartFile(fileContent);

        List<Task> list = xslxParser.parseXlsxFile(multipartFile);

        int expectedSize = 36;
        String expectedJson = getExpectedJson();

        int actualSize = list.size();
        String actualJson = templateInfoToJson(list.get(0));

        assertNotNull(list);
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    @DisplayName("Test, throw XSLXParserException, when file has invalid content.")
    public void testThrowExceptionIfMultipartFileHasInvalidContent() throws IOException {
        String fileName = "test_invalid_content.xlsx";
        byte[] fileContent = getFileContent(fileName);

        MultipartFile multipartFile = getMultipartFile(fileContent);

        Throwable thrown = assertThrows
                (XSLXParserException.class, () -> xslxParser.parseXlsxFile(multipartFile));

        String expectedErrorMessage = "Error occurred while parsing xlsx file";
        String actualErrorMessage = thrown.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @DisplayName("Test, throw XSLXParserException, when file has invalid format.")
    public void testThrowExceptionIfMultipartFileHasInvalidFormat() throws IOException {
        String fileName = "test_invalid_format.docx";
        byte[] fileContent = getFileContent(fileName);

        MultipartFile multipartFile = new MockMultipartFile(
                "test_invalid_format.docx",
                "test_invalid_format.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                fileContent);

        Throwable thrown = assertThrows
                (XSLXParserException.class, () -> xslxParser.parseXlsxFile(multipartFile));

        String expectedErrorMessage = "Error occurred while parsing xlsx file";
        String actualErrorMessage = thrown.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    private static MockMultipartFile getMultipartFile(byte[] fileContent) {
        return new MockMultipartFile(
                "test_invalid_content.xlsx",
                "test_invalid_content.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileContent);
    }

    private String templateInfoToJson(Task templateInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(templateInfo).replaceAll("\r\n", "\n");
    }

    private byte[] getFileContent(String fileName) throws IOException {
        File file = getFile(fileName);
        return FileUtils.readFileToByteArray(file);
    }

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }

    private String getExpectedJson() {
        return """
                {
                  "address" : "Сімферопольська вул., 10, Сімферопольська вул., 10",
                  "client" : "105 школа",
                  "weight" : 145.7
                }""";
    }
}