package cdy.studioapi.dtos;

import java.util.Optional;

public record LocationCreateDto(String name, Optional<Integer> parentId) {

}
