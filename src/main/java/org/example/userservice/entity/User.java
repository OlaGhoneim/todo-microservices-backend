package org.example.userservice.entity;
import jakarta.persistence.*;

@lombok.Getter
@lombok.Setter

@Entity
@Table(name= "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  String id;

    @Column(unique=true, nullable=false)
   private  String email;

     @Column(nullable=false)
    private  String password;

    private boolean enabled=false;

}
