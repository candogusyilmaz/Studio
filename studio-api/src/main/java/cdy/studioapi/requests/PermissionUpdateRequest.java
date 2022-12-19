package cdy.studioapi.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PermissionUpdateRequest {
    private final Integer id;
    private final String displayName;
}
