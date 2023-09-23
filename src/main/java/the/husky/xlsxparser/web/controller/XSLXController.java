package the.husky.xlsxparser.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.Task;
import the.husky.xlsxparser.service.XSLXService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/xlsx")
public class XSLXController {

    private final XSLXService xslxService;

    @GetMapping()
    public String getTemplateForParsing() throws IOException {
        return xslxService.getTemplate();
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> parseXlsxFile(MultipartFile file) {
        return xslxService.getTemplateInfo(file);
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return xslxService.findAllTasks();
    }
}
