package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.dtos.AgencyDto;
import com.Banco.caixaEletronico.models.BankAgency;
import com.Banco.caixaEletronico.service.AgencyService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @PostMapping("/register-agency")
    public ResponseEntity<Object> registerAgency(@RequestBody AgencyDto agencyDto) {
        return ResponseEntity.ok(this.agencyService.registerAgency(agencyDto));
    }

    @GetMapping("/all-agencies")
    public ResponseEntity<List<BankAgency>> getAllAgency() {
        return ResponseEntity.ok(this.agencyService.findAllAgencies());
    }

    @PutMapping ("/replace-agency/{id}")
    public ResponseEntity<Object> replaceAgency(@PathVariable Integer id, @RequestBody AgencyDto agencyDto) {
        return ResponseEntity.ok(this.agencyService.replaceAgency(id, agencyDto));
    }

    @DeleteMapping("/delete-agency/{id}")
    public ResponseEntity<Object> deleteAgency(@PathVariable Integer id) {
        return ResponseEntity.ok(this.agencyService.deleteAgency(id));

    }

    @GetMapping("/list-agency-by-bank/{id}")
    public ResponseEntity<List<BankAgency>> listAgencyByBank(@PathVariable Integer id) {
        return ResponseEntity.ok(this.agencyService.findAgencyByBank(id));
    }




}
