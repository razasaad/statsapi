package org.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stats.dto.Transaction;
import org.stats.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.handleTransaction(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(201).build();
    }
}
