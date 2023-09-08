package com.Banco.caixaEletronico.service;

import com.Banco.caixaEletronico.Enum.TransactionType;
import com.Banco.caixaEletronico.dtos.TransactionDto;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.models.Transactions;
import com.Banco.caixaEletronico.repository.TransactionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
       /// Date dateAtual = new Date();
        Calendar dateAtual = Calendar.getInstance();
        dateAtual.set(Calendar.HOUR_OF_DAY, 0);
        dateAtual.set(Calendar.MINUTE, 0);
        dateAtual.set(Calendar.SECOND, 0);
        dateAtual.set(Calendar.MILLISECOND, 0);
        transactions.setTransactionDate(); // com horas

        switch (transactionType) {
            case DEPOSIT:
                if (Objects.nonNull(transactionDto.getSourceAccountId())) {
                    throw new RuntimeException("Não é necessário informar uma conta de origem!");
                }
                if (Objects.isNull(transactionDto.getTargetAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de destino!");
                }
                transactions.setTargetAccountId(this.accountService.findAccountById(transactionDto.getTargetAccountId()));
                this.depositValueInBalanceOfTargetAccount(transactions.getTargetAccountId(),transactionDto.getTransactionValue());
                transactions.setTransactionValue(transactionDto.getTransactionValue());
                break;
            case WITHDRAW:
                if (Objects.nonNull(transactionDto.getTargetAccountId())) {
                    throw new RuntimeException("Não é necessário informar uma conta de destino, deixe o espaço vazio");
                }
                if (Objects.isNull(transactionDto.getSourceAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de origem ");
                }
                transactions.setSourceAccountId(this.accountService.findAccountById(transactionDto.getSourceAccountId()));
                this.wihtdrawValueInBalanceOfTargetAccount(transactions.getTargetAccountId(),transactionDto.getTransactionValue());
                transactions.setTransactionValue(transactionDto.getTransactionValue().negate());
                break;
            case TRANSFER:
                if (Objects.isNull(transactionDto.getTargetAccountId()) && Objects.isNull(transactionDto.getSourceAccountId())) {
                    throw new RuntimeException("É necessário informar uma conta de destino e uma conta de origem!");
                }
                transactions.setTargetAccountId(this.accountService.findAccountById(transactionDto.getTargetAccountId()));
                transactions.setSourceAccountId(this.accountService.findAccountById(transactionDto.getSourceAccountId()));
                this.transferValueInBalanceOfTarget(transactions.getTargetAccountId(),transactionDto.getTransactionValue(), transactions.getSourceAccountId());
                transactions.setTransactionValue(transactionDto.getTransactionValue());
                break;
        }

        transactions.setTransactionType(transactionType.getDescript());
        return ResponseEntity.ok(this.transactionsRepository.save(transactions));
    }

    private void transferValueInBalanceOfTarget(BankAccount targetAccountId, BigDecimal transactionValue, BankAccount sourceAccountId) {
        if (this.transactionsRepository.countTransferOfSourceAccount(sourceAccountId, TransactionType.TRANSFER)) {
            throw new RuntimeException("Essa conta já atingiu o limite de transferencias!");
        }
        this.compareAccountIsdiferents(sourceAccountId,targetAccountId);
        this.validateIfAccountCanDo(transactionValue,sourceAccountId);
        this.accountService.updateBalance(targetAccountId,transactionValue);
        this.accountService.updateBalance(sourceAccountId,transactionValue.negate());

    }

    private void wihtdrawValueInBalanceOfTargetAccount(BankAccount targetAccountId, BigDecimal transactionValue) {
        if (this.transactionsRepository.countWithdrawOfSourceAccount(targetAccountId, TransactionType.WITHDRAW)) {
            throw new RuntimeException("Essa conta atingiu o limite de saques!");
        }
        this.validateIfAccountCanDo(transactionValue,targetAccountId);
        this.accountService.updateBalance(targetAccountId,transactionValue.negate());
    }

    private void depositValueInBalanceOfTargetAccount(BankAccount targetAccountId, BigDecimal transactionValue) {
        this.accountService.updateBalance(targetAccountId, transactionValue);
    }

    private void compareAccountIsdiferents(BankAccount sourceAccountId, BankAccount targetAccountId) {
        if (sourceAccountId.equals(targetAccountId)) {
            throw new RuntimeException("Não é possivel fazer uma transação para a mesma conta!");
        }
    }

    private void putAccountInTarget(Transactions transactions, Integer targetId) {
        transactions.setTargetAccountId(this.accountService.findAccountById(targetId));
    }

    private void  validateIfAccountCanDo(BigDecimal transactionValue, BankAccount bankAccount) {
      //  BigDecimal dailyBalance = new BigDecimal(this.transactionsRepository.resultOfTransactionsOfCurrentDate(bankAccount));
      //  bankAccount.setTransactionLimit(bankAccount.getBalance().subtract(dailyBalance));








//        if (transactionValue.compareTo(bankAccount.getTransactionLimit()) > 0) {
//            throw new RuntimeException("O limite de operacões de saque ou transacões da conta é insuficeiente!");
//        }
    }
}
