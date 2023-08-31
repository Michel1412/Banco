package com.Banco.caixaEletronico.controller;


import com.Banco.caixaEletronico.dtos.AgencyDto;
import com.Banco.caixaEletronico.models.BankAgency;
import com.Banco.caixaEletronico.service.AgencyService;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @PostMapping("/register-agency")
    public ResponseEntity<Object> registerAgency(@RequestBody AgencyDto agencyDto){
        return ResponseEntity.ok(this.agencyService.registerAgency(agencyDto));
    }





}
