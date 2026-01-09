package dev.joshuaonyema.kaleo.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffProgramResponseDto {
    private UUID id;
    private String name;
}

