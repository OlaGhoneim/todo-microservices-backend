package org.example.todoservice.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.lang.model.element.Name;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "user_id")
    private Long userId;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="item_details_id")
    private ItemDetails itemDetails;
}