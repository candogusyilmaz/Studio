package dev.canverse.studio.api.features.quote.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class CreateQuote {
    private CreateQuote() {
    }

    @Getter
    public static class Request {
        @NotBlank(message = "Content cannot be empty.")
        @Size(min = 24, max = 255, message = "Content must be between {min} and {max} characters.")
        private String content;

        public void setContent(String content) {
            this.content = StringUtils.normalizeSpace(content.trim());
        }
    }
}
