package the.husky.xlsxparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the.husky.xlsxparser.entity.Task;

public interface XLSXRepository extends JpaRepository<Task, Long> {
}
