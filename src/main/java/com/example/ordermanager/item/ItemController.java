package com.example.ordermanager.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping("/")
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequest request){
        Item item = itemService.create(request);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable long itemId,
                                        @RequestBody CreateItemRequest request){
        Item item = itemService.update(itemId, request);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllItems(){
        List<Item> items = itemService.getAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable long itemId){
        Item item = itemService.getItemById(itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> delete(@PathVariable long itemId){
        itemService.delete(itemId);
        return new ResponseEntity<>("Item deleted succesfully", HttpStatus.OK);
    }

}
