package com.Banco.caixaEletronico.controller;

import com.Banco.caixaEletronico.service.AssociateService;
//import com.Banco.caixaEletronico.service.Associates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AssociateController {

    @Autowired
    private AssociateService associeteService;


//    @PostMapping("/register-associate")
//    public ResponseEntity<Object> registerAssociate(@RequestBody Associates associates) {
//        this.associates = associates;
//        return ResponseEntity.ok(this.associeteService.registerAssociate(associates))
//    }


}
