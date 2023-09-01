package com.Banco.caixaEletronico.service;


import com.Banco.caixaEletronico.models.Bank;
import com.Banco.caixaEletronico.repository.AgencyRepository;
import com.Banco.caixaEletronico.repository.BankRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final AgencyRepository agencyRepository;


    public BankService(BankRepository bankRepository, AgencyRepository agencyRepository) {
        this.bankRepository = bankRepository;
        this.agencyRepository = agencyRepository;
    }

    public boolean validateBankExistsByName(String bankName) {
        return this.bankRepository.countBanksByName(bankName);
    }

    public ResponseEntity<Object> registerBank(Bank bank) {
        if (this.validateBankExistsByName(bank.getBankName())) {
            throw new RuntimeException("Banco já cadastrado!");
        }
        return ResponseEntity.ok(this.bankRepository.save(bank));
    }


    public boolean validateBankExistsByBankNumber(Integer bank) {
        return this.bankRepository.countByBankNumber(bank);
    }
    public List<Bank> findAllBanks() {
        if (this.bankRepository.findAll().isEmpty()){
            throw new RuntimeException("Não existem bancos cadastrados!");
        }
        return this.bankRepository.findAll();
    }

    public void deleteById(Integer id) {
        if (this.agencyRepository.countAgencysByBank(id)) {
            throw new RuntimeException("Esse banco ainda possui agencias abertas, é preciso deletalas!");
        }
        this.bankRepository.deleteById(id);
    }

    public Object replaceBank(Integer id, Bank bank) {
        if (this.validateBankExistsByName(bank.getBankName())) {
            throw new RuntimeException("Banco já cadastrado!");
        }
        Bank bankId = this.findBankById(id);
        bankId.setBankName(bank.getBankName());
        return ResponseEntity.ok(this.bankRepository.save(bankId));
    }



    public Bank findBankById(Integer id) {
        return this.bankRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Banco não encontrado!");
        });
    }
}
