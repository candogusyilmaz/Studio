package cdy.studioapi.dtos;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class QuoteCreateDto {
    private String content;

    public String getContent() {
        return StringUtils.normalizeSpace(content);
    }
}
