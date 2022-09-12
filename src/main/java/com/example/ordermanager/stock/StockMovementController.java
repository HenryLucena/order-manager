package com.example.ordermanager.stock;

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

@RestController
@RequestMapping("/stock-movement")
public class StockMovementController {

    @Autowired
    StockMovementService stockMovementService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CreateStockMovementRequest request){
        String stockMovement = stockMovementService.create(request);
        return new ResponseEntity<>(stockMovement, HttpStatus.OK);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<?> update(@PathVariable long stockId,
                                    @RequestBody CreateStockMovementRequest request){
        StockMovement stockMovement = stockMovementService.update(request, stockId);
        return new ResponseEntity<>(stockMovement, HttpStatus.OK);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<?> delete(@PathVariable long stockId){
        stockMovementService.delete(stockId);
        return new ResponseEntity<>("Stock movement successfully deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllStocks(){
        List<StockMovement> stockMovementList = stockMovementService.getAllStocks();
        return new ResponseEntity<>(stockMovementList, HttpStatus.OK);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<?> getStockById(@PathVariable long stockId){
        StockMovement stockMovement = stockMovementService.getStockById(stockId);
        return new ResponseEntity<>(stockMovement, HttpStatus.OK);
    }
}
