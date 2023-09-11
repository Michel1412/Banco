package com.Banco.caixaEletronico.service;

import com.Banco.caixaEletronico.Enum.TransactionType;
import com.Banco.caixaEletronico.dtos.TransactionDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.models.Transactions;
import com.Banco.caixaEletronico.repository.TransactionsRepository;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final AccountService accountService;

    public TransactionsService(TransactionsRepository transactionsRepository, AccountService accountService) {
        this.transactionsRepository = transactionsRepository;
        this.accountService = accountService;
    }

    public Object createTransaction(TransactionDto transactionDto, TransactionType transactionType) {
        if (transactionDto.getTransactionValue().compareTo(new BigDecimal("0")) <= 0) {
            throw new RuntimeException("Não é possivel fazer uma transação com esse valor!");
        }
        Transactions transactions = new Transactions();
        Date dateAtual = new Date();
        transactions.setTransactionDate(dateAtual); // com horas

        switch (transactionType) {
            case DEPOSIT:
                if (Objects.isNull(transactionDto.getTargetAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de destino!");
                }
                transactions.setTargetAccountId(this.accountService.findAccountById(transactionDto.getTargetAccountId()));
                this.depositValueInBalanceOfTargetAccount(transactions.getTargetAccountId(),transactionDto.getTransactionValue());
                break;
            case WITHDRAW:
                if (Objects.isNull(transactionDto.getSourceAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de origem ");
                }
                transactions.setSourceAccountId(this.accountService.findAccountById(transactionDto.getSourceAccountId()));
                this.wihtdrawValueInBalanceOfTargetAccount(transactions.getSourceAccountId(),transactionDto.getTransactionValue());
                break;
            case TRANSFER:
                if (Objects.isNull(transactionDto.getTargetAccountId()) && Objects.isNull(transactionDto.getSourceAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de destino e uma conta de origem!");
                }
                transactions.setTargetAccountId(this.accountService.findAccountById(transactionDto.getTargetAccountId()));
                transactions.setSourceAccountId(this.accountService.findAccountById(transactionDto.getSourceAccountId()));
                this.transferValueInBalanceOfTarget(transactions.getTargetAccountId(),transactionDto.getTransactionValue(), transactions.getSourceAccountId());
                break;
        }
        transactions.setTransactionValue(transactionDto.getTransactionValue());
        transactions.setTransactionType(transactionType.getDescript());
        return ResponseEntity.ok(this.transactionsRepository.save(transactions));
    }

    private void transferValueInBalanceOfTarget(BankAccount targetAccountId, BigDecimal transactionValue, BankAccount sourceAccountId) {
        this.validateNumberOfWithdrawOrTransfer(sourceAccountId.getId(), TransactionType.TRANSFER.getDescript());
        this.compareAccountIsdiferents(sourceAccountId,targetAccountId);
        this.validateIfAccountCanDo(transactionValue,sourceAccountId);
        this.accountService.updateBalance(targetAccountId,transactionValue);
        this.accountService.updateBalance(sourceAccountId,transactionValue);

    }

    private void wihtdrawValueInBalanceOfTargetAccount(BankAccount sourceAccountId, BigDecimal transactionValue) {
        this.validateNumberOfWithdrawOrTransfer(sourceAccountId.getId(), TransactionType.WITHDRAW.getDescript());
        this.validateIfAccountCanDo(transactionValue,sourceAccountId);
        this.accountService.updateBalance(sourceAccountId,transactionValue.negate());
    }

    private void depositValueInBalanceOfTargetAccount(BankAccount targetAccountId, BigDecimal transactionValue) {
        this.accountService.updateBalance(targetAccountId, transactionValue);
    }

    private void compareAccountIsdiferents(BankAccount sourceAccountId, BankAccount targetAccountId) {
        if (sourceAccountId.equals(targetAccountId)) {
            throw new RuntimeException("Não é possivel fazer uma transação para a mesma conta!");
        }
    }

    private void  validateIfAccountCanDo(BigDecimal transactionValue, BankAccount bankAccount) {

        BigDecimal balanceNow = this.accountService.findBalanceById(bankAccount);
        BigDecimal subtractTransactions = this.transactionsRepository.sumTransferOfSourceAccount(bankAccount.getId());
        BigDecimal addTransactions = this.transactionsRepository.sumTransferOfTargetAccount(bankAccount.getId());
        BigDecimal totalTransactions = subtractTransactions.subtract(addTransactions);
        BigDecimal dailyInitialBalance = balanceNow.add(totalTransactions);
        BigDecimal transactionLimit = dailyInitialBalance.multiply(new BigDecimal(".3"));
        BigDecimal limitResult = transactionLimit.subtract(subtractTransactions);

        if (limitResult.compareTo(transactionValue) < 0) {
            throw new RuntimeException("Não é possivel realizar a operação seu limite diario é menor");
        }
    }

    public void validateNumberOfWithdrawOrTransfer(Integer sourceAccountId, String transactionType) {

        if (this.transactionsRepository.countTransferOfSourceAccount(sourceAccountId, transactionType)) {
            throw new RuntimeException("Essa conta já atingiu o limite de transferencias!");
        }
    }
}
