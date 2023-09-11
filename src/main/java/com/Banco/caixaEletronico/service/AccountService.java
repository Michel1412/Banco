package com.Banco.caixaEletronico.service;


import com.Banco.caixaEletronico.dtos.AccountDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.repository.AccontRepository;
import com.Banco.caixaEletronico.repository.AgencyRepository;
import com.Banco.caixaEletronico.repository.AssociateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccontRepository accountRepository;

    private final AssociateService associateService;

    private final AgencyService agencyService;

    public AccountService(AccontRepository accountRepository,
                          AssociateService associateService,
                          AgencyService agencyService) {
        this.accountRepository = accountRepository;
        this.associateService = associateService;
        this.agencyService = agencyService;
    }


    public Object createAccount(AccountDto accountDto) {
        if (this.accountRepository.countAccountByAccountNumber(accountDto.getAccountNumber(), accountDto.getAgencyId())) {
            throw new RuntimeException("Essa conta já foi criada!");
        }
        this.associateService.validateAssociateById(accountDto.getAssociateId());
        if (this.accountRepository.countAccountOnAgencyByBankAndAssociateId(accountDto.getAssociateId(),accountDto.getAgencyId())) {
            throw new RuntimeException("Esse associado já faz parte desse banco!");
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(accountDto.getAccountNumber());
        bankAccount.setAgencyId(this.agencyService.findAgencyById(accountDto.getAgencyId()));
        bankAccount.setAssociateId(this.associateService.findAssociateById(accountDto.getAssociateId()));
        bankAccount.setBalance(accountDto.getBalance());
        return ResponseEntity.ok(this.accountRepository.save(bankAccount));
    }

    public List<BankAccount> findAllAccounts() {
        if (this.accountRepository.findAll().isEmpty()) {
            throw new RuntimeException("Não existem contas cadastrados!");
        }
        return this.accountRepository.findAll();
    }

    public Object updateAccount(Integer id, AccountDto accountDto) {
        BankAccount updateAccount = this.findAccountById(id);
        this.accountRepository.countAccountByAccountNumber(accountDto.getAccountNumber(), accountDto.getAgencyId());
        updateAccount.setBalance(accountDto.getBalance());
        updateAccount.setAccountNumber(accountDto.getAccountNumber());
        updateAccount.setAgencyId(this.agencyService.findAgencyById(accountDto.getAgencyId()));
        updateAccount.setAssociateId(this.associateService.findAssociateById(accountDto.getAssociateId()));
        return ResponseEntity.ok(this.accountRepository.save(updateAccount));
    }

    public BankAccount findAccountById(Integer id) {
        return this.accountRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Essa conta não existe!");
        });
    }

    public String deleteAccount(Integer id) {
        this.accountRepository.deleteById(id);
        return "Sua conta foi deletada com sucesso!";
    }

    public void updateBalance(BankAccount targetAccountId, BigDecimal transactionValue) {

        targetAccountId.setBalance(targetAccountId.getBalance().add(transactionValue));
        this.accountRepository.save(targetAccountId);
    }

    public BigDecimal findBalanceById(BankAccount bankAccount) {
        return this.accountRepository.findBalanceById(bankAccount);
    }

}
