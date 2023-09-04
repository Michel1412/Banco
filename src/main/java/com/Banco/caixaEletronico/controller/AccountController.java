package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.dtos.AccountDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController {

    @Autowired
    public AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<Object> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(this.accountService.createAccount(accountDto));
    }


}
