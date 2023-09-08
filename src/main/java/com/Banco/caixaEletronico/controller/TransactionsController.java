package com.Banco.caixaEletronico.controller;

import com.Banco.caixaEletronico.Enum.TransactionType;
import com.Banco.caixaEletronico.dtos.TransactionDto;
import com.Banco.caixaEletronico.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/create-deposit")
    public ResponseEntity<Object> createDeposit(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(this.transactionsService.createTransaction(transactionDto, TransactionType.DEPOSIT));
    }
    @PostMapping("/create-withdraw")
    public ResponseEntity<Object> createWithdraw(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(this.transactionsService.createTransaction(transactionDto, TransactionType.WITHDRAW));
    }
    @PostMapping("/create-transfer")
    public ResponseEntity<Object> createTransfer(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(this.transactionsService.createTransaction(transactionDto, TransactionType.TRANSFER));
    }
}
