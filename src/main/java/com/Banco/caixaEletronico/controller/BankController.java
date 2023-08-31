package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.models.Bank;
import com.Banco.caixaEletronico.repository.BankRepository;
import com.Banco.caixaEletronico.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping(value = "/register-bank")
    public ResponseEntity<Object> registerBank(@RequestBody Bank bank ){
        return ResponseEntity.ok(this.bankService.registerBank(bank));
    }

    @GetMapping(value = "/all-banks")
    public ResponseEntity<List<Bank>> getAllBanks(@RequestBody Bank bank){
        return ResponseEntity.ok(this.bankService.findAllBanks());
    }

    @DeleteMapping(value = "/delete-bank/{id}")
    public void deleteBank(@PathVariable Integer id){
        this.bankService.deleteById(id);
    }

    @PutMapping(value = "/replace-bank/{id}")
    public ResponseEntity<Object> replaceBank(@PathVariable Integer id,@RequestBody Bank bank){
        return ResponseEntity.ok(this.bankService.replaceBank(id, bank));
    }

}
