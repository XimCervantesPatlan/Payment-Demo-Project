
package com.example.persistence;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Transaction transaction) {
        Transaction saved = repository.save(transaction);
        return ResponseEntity.status(201).body(
            java.util.Map.of("status", "SAVED", "dbId", saved.getId())
        );
    }
}
