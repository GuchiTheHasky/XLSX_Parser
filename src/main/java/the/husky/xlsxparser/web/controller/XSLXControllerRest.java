package the.husky.xlsxparser.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.TemplateInfo;
import the.husky.xlsxparser.service.XSLXService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class XSLXControllerRest {

    private final XSLXService xslxService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TemplateInfo> parseXlsxFile(MultipartFile file) {
        return xslxService.getTemplateInfo(file);
    }
}
