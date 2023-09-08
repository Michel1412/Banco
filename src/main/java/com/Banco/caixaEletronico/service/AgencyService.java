package com.Banco.caixaEletronico.service;


import com.Banco.caixaEletronico.dtos.AgencyDto;
import com.Banco.caixaEletronico.models.BankAgency;
import com.Banco.caixaEletronico.repository.AccontRepository;
import com.Banco.caixaEletronico.repository.AgencyRepository;
import com.Banco.caixaEletronico.repository.BankRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final BankRepository bankRepository;
    private final BankService bankService;
    private final AccontRepository accountRepository;



    public AgencyService(AgencyRepository agencyRepository, BankRepository bankRepository, BankService bankService, AccontRepository accountRepository) {
        this.agencyRepository = agencyRepository;
        this.bankRepository = bankRepository;
        this.bankService = bankService;
        this.accountRepository = accountRepository;
    }

    public Object registerAgency(AgencyDto agencyDto) {
        if (Objects.isNull(agencyDto.getBankNumber())) {
            throw new RuntimeException("É necessário informar um banco!");
        }

        if (!this.bankRepository.countByBankNumber(agencyDto.getBankNumber())) {
            throw new RuntimeException("Para cadastrar uma agência, é necessário um banco cadastrado!");
        }

        if (this.validateAgencyExists(agencyDto)){
            throw new RuntimeException("Agencia já cadastrada para esse banco!");
        }
        BankAgency bankAgency = new BankAgency();
        bankAgency.setAgencyNumber(agencyDto.getAgencyNumber());
        bankAgency.setBankNumber(this.bankService.findBankById(agencyDto.getBankNumber()));
        return ResponseEntity.ok(this.agencyRepository.save(bankAgency));
    }


    public List<BankAgency> findAllAgencies() {
        if (this.agencyRepository.findAll().isEmpty()) {
            throw new RuntimeException("Não exite agencias cadastradas!");
        }
        return this.agencyRepository.findAll();
    }

    public Object replaceAgency(Integer id, AgencyDto agencyDto) {

        if (this.validateAgencyExists(agencyDto)) {
            throw  new RuntimeException("Agencia já cadastrada!");
        }
        BankAgency renameAgency = this.findAgencyById(id);
        renameAgency.setAgencyNumber(agencyDto.getAgencyNumber());
        return ResponseEntity.ok(this.agencyRepository.save(renameAgency));
    }

    public BankAgency findAgencyById(Integer id) {
        return this.agencyRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Agência não encontrada!");
        });
    }

    public boolean validateAgencyExists(AgencyDto agencyDto) {
        return this.agencyRepository.countAgencysByAgencyNumberAndBank(agencyDto.getAgencyNumber(), agencyDto.getBankNumber());
    }


    public String deleteAgency(Integer id) {
        if (this.accountRepository.countAllByAgency(id)) {
            throw new RuntimeException("Não é posivel deleta, essa agencia tem contas cadastradas!");
        }
        this.agencyRepository.deleteById(id);
        return "Agencia deletada com sucesso!";
    }

    public List<BankAgency> findAgencyByBank(Integer id) {
        if (!this.bankService.validateBankExistsByBankNumber(id)) {
            throw new RuntimeException("Esse banco não exite!");
        }

        if (this.agencyRepository.findAllByBank(id).isEmpty()) {
          throw new RuntimeException("Esse banco não tem agencias!");
        }
        return this.agencyRepository.findAllByBank(id);
    }
}
