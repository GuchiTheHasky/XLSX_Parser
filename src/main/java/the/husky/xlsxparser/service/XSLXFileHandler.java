package the.husky.xlsxparser.service;

import org.springframework.web.multipart.MultipartFile;
import the.husky.xlsxparser.entity.Task;

import java.util.List;

/**
 * The XSLXFileHandler interface defines a contract for services that handle XSLX (Excel) files
 * and extract template information from them.
 */

public interface XSLXFileHandler {

    /**
     * Retrieves template information from an XSLX file.
     *
     * @param file The XSLX file to process.
     * @return A list of {@link Task} objects extracted from the file.
     */

    List<Task> getTemplateInfo(MultipartFile file);

    /**
     * Retrieves a list of all tasks.
     *
     * @return A list of {@link Task} objects representing all tasks.
     */

    List<Task> findAllTasks();
}
