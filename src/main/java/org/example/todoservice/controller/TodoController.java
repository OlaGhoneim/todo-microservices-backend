package org.example.todoservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todoservice.exception.ResourceNotFoundException;
import org.example.todoservice.model.request.ItemRequest;
import org.example.todoservice.model.response.ItemResponse;
import org.example.todoservice.service.TodoService;
import org.example.todoservice.service.TokenValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final TokenValidationService tokenValidationService;

    @PostMapping("/add")
    public ResponseEntity<?> addItem(
            @Valid @RequestBody ItemRequest request,
            @RequestHeader("Authorization") String token) {
        if (!tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
        ItemResponse response = todoService.addItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        if (!tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
        todoService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request,
            @RequestHeader("Authorization") String token) {
        if (!tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
        ItemResponse response = todoService.updateItem(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> searchById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        if (!tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
        ItemResponse response = todoService.searchById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTitle(
            @RequestParam String title,
            @RequestHeader("Authorization") String token) {
        if (!tokenValidationService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
        List<ItemResponse> response = todoService.searchByTitle(title);
        return ResponseEntity.ok(response);
    }
}