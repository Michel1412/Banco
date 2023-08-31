package com.Banco.caixaEletronico.service;


import com.Banco.caixaEletronico.dtos.AgencyDto;
import com.Banco.caixaEletronico.models.BankAgency;
import com.Banco.caixaEletronico.repository.AgencyRepository;
import com.Banco.caixaEletronico.repository.BankRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final BankRepository bankRepository;
    private final BankService bankService;


    public AgencyService(AgencyRepository agencyRepository, BankRepository bankRepository, BankService bankService) {
        this.agencyRepository = agencyRepository;
        this.bankRepository = bankRepository;
        this.bankService = bankService;
    }

    public Object registerAgency(AgencyDto agencyDto) {
        if (Objects.isNull(agencyDto.getBankNumber())) {
            throw new RuntimeException("É necessário informar um banco!");
        }

        if (!this.bankRepository.countByBankNumber(agencyDto.getBankNumber())) {
            throw new RuntimeException("Para cadastrar uma agência, é necessário um banco cadastrado!");
        }

        if (this.agencyRepository.countAgencysByAgencyNumberAndBank(agencyDto.getAgencyNumber(), agencyDto.getBankNumber())){
            throw new RuntimeException("Agencia já cadastrada para esse banco!");
        }
        BankAgency bankAgency = new BankAgency();
        bankAgency.setAgencyNumber(agencyDto.getAgencyNumber());
        bankAgency.setBankNumber(this.bankService.findBankById(agencyDto.getBankNumber()));
        return ResponseEntity.ok(this.agencyRepository.save(bankAgency));
    }



    public boolean validateAgencyByNumber(String bankAgency) {
        return this.agencyRepository.countAgencysByNumber(bankAgency);

    }




}
