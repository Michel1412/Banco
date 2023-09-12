package com.Banco.caixaEletronico.controller;

import com.Banco.caixaEletronico.models.Associates;
import com.Banco.caixaEletronico.service.AssociateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AssociateController {

    private final AssociateService associeteService;

    public AssociateController(AssociateService associeteService) {
        this.associeteService = associeteService;
    }

    @PostMapping("/register-associate")
    public ResponseEntity<Object> registerAssociate(@RequestBody Associates associates) {
        return ResponseEntity.ok(this.associeteService.registerAssociate(associates));
    }

    @GetMapping("/all-associates")
    public ResponseEntity<List<Associates>> getAllAssociates() {
        return ResponseEntity.ok(this.associeteService.findAllAssociates());
    }

    @GetMapping("/details-associate/{id}")
    public ResponseEntity<Associates> getAssociateById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.associeteService.findAssociateById(id));
    }

    @PutMapping("/replace-associate/{id}")
    public ResponseEntity<Object> replaceAssociateById(@PathVariable Integer id, @RequestBody Associates associates) {
        return ResponseEntity.ok(this.associeteService.replaceAssociate(id, associates));
    }

    @DeleteMapping("/delete-associate/{id}")
    public ResponseEntity<Object> deleteAssociate(@PathVariable Integer id) {
        return ResponseEntity.ok(this.associeteService.deleteAssociate(id));
    }



}
