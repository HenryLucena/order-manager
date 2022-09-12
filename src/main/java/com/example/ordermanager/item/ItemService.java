package com.example.ordermanager.item;

import com.example.ordermanager.configuration.handler.InternalServerErrorException;
import com.example.ordermanager.configuration.handler.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ItemService {

    private static final Logger errorLog = LogManager.getLogger("errorLogger");


    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public Item create(CreateItemRequest request){
        try {
            Item item = new Item(request);
            itemRepository.save(item);
            return item;
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @Transactional
    public Item update(long itemID, CreateItemRequest request) {
        try {
            Item item = itemRepository.findById(itemID).get();

            if(isNull(item)) throw new NotFoundException("Item not found");

            item.setName(request.getName());
            itemRepository.save(item);

            return item;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public Item getItemById(long itemId) {
        try {
            Item item = itemRepository.findById(itemId).get();

            if(isNull(item)) throw new NotFoundException("Item not found");

            return item;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(long itemId) {
        try {
            Item item = itemRepository.findById(itemId).get();

            if(isNull(item)) throw new NotFoundException("Item not found");

            itemRepository.delete(item);
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public List<Item> getAll() {
        try {
            List<Item> items = itemRepository.findAll();

            if(items.isEmpty()) throw new NotFoundException("No item found");

            return items;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }
}
