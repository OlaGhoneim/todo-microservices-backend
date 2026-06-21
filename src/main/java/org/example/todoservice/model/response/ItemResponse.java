package org.example.todoservice.model.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String title;
    private Long userId;
    private String description;
    private LocalDateTime createdAt;
    private String priority;
    private String status;
}