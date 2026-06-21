package org.example.todoservice;

import org.example.todoservice.entity.Item;
import org.example.todoservice.entity.ItemDetails;
import org.example.todoservice.exception.ResourceNotFoundException;
import org.example.todoservice.model.request.ItemRequest;
import org.example.todoservice.model.response.ItemResponse;
import org.example.todoservice.repository.ItemRepository;
import org.example.todoservice.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceApplicationTests {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private TodoService todoService;

    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        ItemDetails details = new ItemDetails();
        details.setId(1L);
        details.setDescription("Test description");
        details.setCreatedAt(LocalDateTime.now());
        details.setPriority("HIGH");
        details.setStatus("PENDING");

        item = new Item();
        item.setId(1L);
        item.setTitle("Test task");
        item.setUserId(1L);
        item.setItemDetails(details);

        itemRequest = new ItemRequest();
        itemRequest.setTitle("Test task");
        itemRequest.setUserId(1L);
        itemRequest.setDescription("Test description");
        itemRequest.setPriority("HIGH");
        itemRequest.setStatus("PENDING");
    }

    @Test
    void addItem_ShouldReturnItemResponse() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponse response = todoService.addItem(itemRequest);

        assertNotNull(response);
        assertEquals("Test task", response.getTitle());
        assertEquals("HIGH", response.getPriority());
        assertEquals("PENDING", response.getStatus());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void searchById_ShouldReturnItemResponse_WhenItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemResponse response = todoService.searchById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test task", response.getTitle());
    }

    @Test
    void searchById_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> todoService.searchById(99L));
    }

    @Test
    void deleteItem_ShouldDeleteSuccessfully_WhenItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).delete(item);

        assertDoesNotThrow(() -> todoService.deleteItem(1L));
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void deleteItem_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> todoService.deleteItem(99L));
    }

    @Test
    void updateItem_ShouldReturnUpdatedResponse_WhenItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        itemRequest.setTitle("Updated task");
        ItemResponse response = todoService.updateItem(1L, itemRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> todoService.updateItem(99L, itemRequest));
    }

    @Test
    void searchByTitle_ShouldReturnList_WhenItemsExist() {
        when(itemRepository.findByTitleContainingIgnoreCase("Test"))
                .thenReturn(List.of(item));

        List<ItemResponse> responses = todoService.searchByTitle("Test");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test task", responses.get(0).getTitle());
    }

    @Test
    void searchByTitle_ShouldThrowException_WhenNoItemsFound() {
        when(itemRepository.findByTitleContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> todoService.searchByTitle("xyz"));
    }
}



