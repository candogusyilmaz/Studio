package cdy.studioapi.requests;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class QuoteCreateRequest {
    private String content;

    public void setContent(String content) {
        this.content = StringUtils.normalizeSpace(content.trim());
    }
}
