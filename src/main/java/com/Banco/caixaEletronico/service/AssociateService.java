package com.Banco.caixaEletronico.service;

import com.Banco.caixaEletronico.repository.AssociateRepository;
import org.springframework.stereotype.Service;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;


    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }
}
