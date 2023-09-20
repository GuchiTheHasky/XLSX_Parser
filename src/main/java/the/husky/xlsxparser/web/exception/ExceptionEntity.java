package the.husky.xlsxparser.web.exception;

import org.springframework.http.HttpStatus;

public record ExceptionEntity(String message, HttpStatus httpStatus, int httpStatusCode) {
}
