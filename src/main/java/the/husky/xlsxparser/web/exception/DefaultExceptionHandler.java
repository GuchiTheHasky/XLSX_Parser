package the.husky.xlsxparser.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(XSLXParserException.class)
    public ResponseEntity<ExceptionEntity> handleXSLXParserException(XSLXParserException e) {
        String message = e.getMessage();
        ExceptionEntity exception = new ExceptionEntity(
                message,
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
}

