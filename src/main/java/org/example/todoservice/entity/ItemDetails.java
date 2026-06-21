package org.example.todoservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String priority;

    private String status;
}
