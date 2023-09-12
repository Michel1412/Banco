package com.Banco.caixaEletronico.service;

import com.Banco.caixaEletronico.Enum.TransactionType;
import com.Banco.caixaEletronico.dtos.TransactionDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.models.Transactions;
import com.Banco.caixaEletronico.repository.TransactionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Service
public class TransactionsService {

    Date dateToday = new Date();
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
        transactions.setTransactionDate(dateToday); // com horas

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
        transactions.setStatus("aprovado");
        return ResponseEntity.ok(this.transactionsRepository.save(transactions));
    }

    private void transferValueInBalanceOfTarget(BankAccount targetAccountId, BigDecimal transactionValue, BankAccount sourceAccountId) {
        this.validateNumberOfWithdrawOrTransfer(sourceAccountId.getId(), TransactionType.TRANSFER.getDescript());
        this.compareAccountIsdiferents(sourceAccountId,targetAccountId);
        this.validateFullBalanceTransaction(transactionValue,sourceAccountId);
        this.accountService.updateBalance(targetAccountId,transactionValue);
        this.accountService.updateBalance(sourceAccountId,transactionValue.negate());

    }

    private void wihtdrawValueInBalanceOfTargetAccount(BankAccount sourceAccountId, BigDecimal transactionValue) {
        this.validateNumberOfWithdrawOrTransfer(sourceAccountId.getId(), TransactionType.WITHDRAW.getDescript());
        this.validateFullBalanceTransaction(transactionValue,sourceAccountId);
        this.accountService.updateBalance(sourceAccountId,transactionValue.negate());
    }

    private void validateFullBalanceTransaction(BigDecimal transactionValue, BankAccount sourceAccountId) {
        BigDecimal balanceNow = this.accountService.findBalanceById(sourceAccountId);
        BigDecimal fullBalanceTransaction = this.transactionsRepository.findFullBalnceByAccountId(sourceAccountId.getId());
        if (balanceNow.compareTo(fullBalanceTransaction) > 0) {
            this.validateIfAccountCanDo(transactionValue,sourceAccountId);
        } else {
            if (transactionValue.compareTo(balanceNow) > 0) {
                throw new RuntimeException("A conta não tem saldo suficiente para fazer o saque!");
            }
        }
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

        if (this.transactionsRepository.countTransactionsOfSourceAccount(sourceAccountId, transactionType)) {
            throw new RuntimeException("Essa conta já atingiu o limite de transferencias!");
        }
    }

    public Object reversalTransfer(Integer id) {
        Transactions transferForReversal = this.transactionsRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Essa transação não foi encontrada!");});
        BigDecimal reversalValue = transferForReversal.getTransactionValue();
        this.accountService.updateBalance(transferForReversal.getTargetAccountId(), reversalValue.negate());
        this.accountService.updateBalance(transferForReversal.getSourceAccountId(), reversalValue);
        Transactions reversalSaveTransfer = new Transactions();
        reversalSaveTransfer.setTargetAccountId(transferForReversal.getTargetAccountId());
        reversalSaveTransfer.setSourceAccountId(transferForReversal.getSourceAccountId());
        reversalSaveTransfer.setTransactionValue(reversalValue);
        if (this.transactionsRepository.countReversalTransferByTargetAndSourceId(reversalSaveTransfer.getTargetAccountId(),reversalSaveTransfer.getSourceAccountId(), reversalValue)) {
            throw new RuntimeException("Essa transação já foi estornada!");
        }
        reversalSaveTransfer.setTransactionDate(dateToday);
        reversalSaveTransfer.setStatus("aprovado");
        reversalSaveTransfer.setTransactionType("Estorno");
        transferForReversal.setStatus("estornado");
        this.transactionsRepository.save(transferForReversal);
        return ResponseEntity.ok(this.transactionsRepository.save(reversalSaveTransfer));
    }
}
