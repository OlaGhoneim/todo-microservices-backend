package org.example.todoservice.repository;

import org.example.todoservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> searchItemByTitleIgnoreCaseContaining(String title);
}