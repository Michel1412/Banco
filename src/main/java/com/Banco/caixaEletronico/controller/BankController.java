package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.models.Bank;
import com.Banco.caixaEletronico.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping(value = "/register-bank")
    public ResponseEntity<Object> registerBank(@RequestBody Bank bank ){
        return ResponseEntity.ok(this.bankService.registerBank(bank));
    }

    @GetMapping(value = "/all-banks")
    public ResponseEntity<List<Bank>> getAllBanks(){
        return ResponseEntity.ok(this.bankService.findAllBanks());
    }

    @DeleteMapping(value = "/delete-bank/{id}")
    public ResponseEntity<Object> deleteBank(@PathVariable Integer id){
        return ResponseEntity.ok(this.bankService.deleteById(id));
    }

    @PutMapping(value = "/replace-bank/{id}")
    public ResponseEntity<Object> replaceBank(@PathVariable Integer id,@RequestBody Bank bank){
        return ResponseEntity.ok(this.bankService.replaceBank(id, bank));
    }

}
