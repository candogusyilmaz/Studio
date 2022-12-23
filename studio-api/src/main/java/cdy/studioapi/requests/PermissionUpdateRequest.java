package cdy.studioapi.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record PermissionUpdateRequest(Integer id, String displayName) {
}
