package com.Banco.caixaEletronico.service;


import com.Banco.caixaEletronico.dtos.AccountDto;
import com.Banco.caixaEletronico.dtos.AgencyDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.repository.AccontRepository;
import com.Banco.caixaEletronico.repository.AgencyRepository;
import com.Banco.caixaEletronico.repository.AssociateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccontRepository accountRepository;

    private final AgencyRepository agencyRepository;

    private final AssociateRepository associateRepository;

    private final AssociateService associateService;

    private final AgencyService agencyService;

    public AccountService(AccontRepository accountRepository,
                          AgencyRepository agencyRepository,
                          AssociateRepository associateRepository,
                          AssociateService associateService,
                          AgencyService agencyService) {
        this.accountRepository = accountRepository;
        this.agencyRepository = agencyRepository;
        this.associateRepository = associateRepository;
        this.associateService = associateService;
        this.agencyService = agencyService;
    }


    public Object createAccount(AccountDto accountDto) {
        if (this.accountRepository.countAccountByAccountNumber(accountDto.getAccountNumber())) {
            throw new RuntimeException("Essa conta já foi criada!");
        }
        if (this.accountRepository.validateAgencyExistsById(accountDto.getAgencyId())) {
            throw new RuntimeException("Essa agencia não foi encontrada!");
        }
        if (this.accountRepository.validadeAssociateById(accountDto.getAssociateId())) {
            throw new RuntimeException("Esse associado não foi encontrado!");
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(accountDto.getAccountNumber());
        bankAccount.setAgencyId(this.agencyService.findAgencyById(accountDto.getAgencyId()));
        bankAccount.setAssociateId(this.associateService.findAssociateById(accountDto.getAssociateId()));
        bankAccount.setBalance(accountDto.getBalance());
        bankAccount.setTransactionLimit(accountDto.getTransactionLimit());
        return ResponseEntity.ok(this.accountRepository.save(bankAccount));
    }
}
