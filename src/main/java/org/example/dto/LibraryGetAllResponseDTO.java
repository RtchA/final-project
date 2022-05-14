package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class LibraryGetAllResponseDTO {
    private long id;
    private long movieId;
    private String name;
    private String preview;
    private String file;
    private String genre;
    private int dateRelease;
}
