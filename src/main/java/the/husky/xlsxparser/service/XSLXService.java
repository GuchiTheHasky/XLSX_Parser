package the.husky.xlsxparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.TemplateInfo;
import the.husky.xlsxparser.parser.XSLXParser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XSLXService implements XSLXFileHandler {
    private final XSLXParser xslxParser;

    public List<TemplateInfo> getTemplateInfo(MultipartFile file, int sheetIndex) {
        return xslxParser.parseXlsxFile(file, sheetIndex);
    }
}
