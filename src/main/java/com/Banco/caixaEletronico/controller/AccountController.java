package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.dtos.AccountDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<Object> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(this.accountService.createAccount(accountDto));
    }

    @GetMapping("/all-accounts")
    public ResponseEntity<List<BankAccount>> findAllAccounts() {
        return ResponseEntity.ok(this.accountService.findAllAccounts());
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<Object> updateaccount(@PathVariable Integer id,@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(this.accountService.updateAccount(id, accountDto));
    }

    @GetMapping("/detais-account/{id}")
    public ResponseEntity<Object> findAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.accountService.findAccountById(id));
    }

    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Integer id) {
        return ResponseEntity.ok(this.accountService.deleteAccount(id));
    }

}
