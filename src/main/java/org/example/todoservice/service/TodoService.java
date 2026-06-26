package org.example.todoservice.service;

import lombok.RequiredArgsConstructor;
import org.example.todoservice.entity.Item;
import org.example.todoservice.entity.ItemDetails;
import org.example.todoservice.exception.ResourceNotFoundException;
import org.example.todoservice.model.request.ItemRequest;
import org.example.todoservice.model.response.ItemResponse;
import org.example.todoservice.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final ItemRepository itemRepository;

    public ItemResponse addItem(ItemRequest request) {
        ItemDetails details = new ItemDetails();
        details.setDescription(request.getDescription());
        details.setCreatedAt(LocalDateTime.now());
        details.setPriority(request.getPriority());
        details.setStatus(request.getStatus());

        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setUserId(request.getUserId());
        item.setItemDetails(details);

        Item saved = itemRepository.save(item);
        return mapToResponse(saved);

    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        itemRepository.delete(item);
    }

    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        item.setTitle(request.getTitle());
        item.setUserId(request.getUserId());
        item.getItemDetails().setDescription(request.getDescription());
        item.getItemDetails().setPriority(request.getPriority());
        item.getItemDetails().setStatus(request.getStatus());

        Item updated = itemRepository.save(item);
        return mapToResponse(updated);
    }

    public ItemResponse searchById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return mapToResponse(item);
    }

    public List<ItemResponse> searchByTitle(String title) {
        List<Item> items = itemRepository.searchItemByTitleIgnoreCaseContaining(title);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("No items found with title: " + title);
        }
        return items.stream().map(this::mapToResponse).toList();
    }

    private ItemResponse mapToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setUserId(item.getUserId());
        response.setDescription(item.getItemDetails().getDescription());
        response.setCreatedAt(item.getItemDetails().getCreatedAt());
        response.setPriority(item.getItemDetails().getPriority());
        response.setStatus(item.getItemDetails().getStatus());
        return response;
    }
}
