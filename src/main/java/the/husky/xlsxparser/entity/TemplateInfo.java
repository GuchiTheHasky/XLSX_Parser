package the.husky.xlsxparser.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateInfo {
    private String address;
    private String client;
    private Double weight;
}
