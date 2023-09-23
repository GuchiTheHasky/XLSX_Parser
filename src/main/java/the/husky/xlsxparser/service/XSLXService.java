package the.husky.xlsxparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.Task;
import the.husky.xlsxparser.parser.XSLXParser;
import the.husky.xlsxparser.repository.XLSXRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XSLXService implements XSLXFileHandler {
    private final XSLXParser xslxParser;
    private final XLSXRepository xlsxRepository;

    public List<Task> getTemplateInfo(MultipartFile file) {
        List<Task> tasks = xslxParser.parseXlsxFile(file);
        return xlsxRepository.saveAll(tasks);
    }

    public List<Task> findAllTasks() {
        return xlsxRepository.findAll();
    }

    public String getTemplate() throws IOException {
        Resource resource = new ClassPathResource("templates/index.html");
        return Files.readString(Paths.get(resource.getURI()));
    }
}
