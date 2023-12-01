package dev.cdy.studio.api.features.quote.dtos;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class CreateQuote {
    private CreateQuote() {
    }

    @Getter
    public static class Request {
        private String content;

        public void setContent(String content) {
            this.content = StringUtils.normalizeSpace(content.trim());
        }
    }
}
