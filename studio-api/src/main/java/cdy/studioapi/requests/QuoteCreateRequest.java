package cdy.studioapi.requests;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class QuoteCreateRequest {
    private String content;

    public String getContent() {
        return StringUtils.normalizeSpace(content);
    }
}
