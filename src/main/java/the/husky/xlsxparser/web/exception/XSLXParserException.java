package the.husky.xlsxparser.web.exception;

public class XSLXParserException extends RuntimeException {
    public XSLXParserException(String message) {
        super(message);
    }
    public XSLXParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
