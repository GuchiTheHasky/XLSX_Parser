package the.husky.xlsxparser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.TemplateInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("XSLXParser unit tests")
class XSLXParserTest {
    private final static String LOCATION = "280 школа Євгенія Харченка вул., 23Б, Євгенія Харченка вул., 23Б";
    private final static String LOCATION2 = "Київська Русь Гімназія Бориса Гмирі вул., 2В, Бориса Гмирі вул., 2В";

    private static XSLXParser xslxParser;

    @BeforeAll
    public static void init() {
        xslxParser = new XSLXParser();
    }

    @Test
    @DisplayName("Should parse xlsx file")
    public void test1() {
        String expected = "Євгенія Харченка вул., 23Б, Євгенія Харченка вул., 23Б";
        String actual = xslxParser.getAddress(LOCATION);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should parse xlsx file")
    public void test2() {
        String expected = "280 школа";
        String actual = xslxParser.getClientName(LOCATION);

        assertEquals(expected, actual);

        String expected2 = "Київська Русь";
        String actual2 = xslxParser.getClientName(LOCATION2);

        assertEquals(expected2, actual2);
    }

    @Test
    @DisplayName("Should parse xlsx file")
    public void test3() throws IOException {
        String fileName = "test_template.xlsx";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        byte[] fileContent = FileUtils.readFileToByteArray(file);

        MultipartFile multipartFile = new MockMultipartFile(
                "test_template.xlsx",
                "test_template.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileContent);

        List<TemplateInfo> list = xslxParser.parseXlsxFile(multipartFile);

        int expectedSize = 36;
        String expectedJson = getExpectedJson();

        int actualSize = list.size();
        String actualJson = templateInfoToJson(list.get(0));

        assertNotNull(list);
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedJson, actualJson);
    }


    private String templateInfoToJson(TemplateInfo templateInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(templateInfo).replaceAll("\r\n", "\n");
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