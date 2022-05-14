package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieUpdateResponseDTO {
    private long id;
    private String name;
    private String preview;
    private String file;
    private String genre;
    private int dateRelease;
    private int price;
    private boolean removed;
}
