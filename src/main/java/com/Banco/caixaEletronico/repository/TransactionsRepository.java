package com.Banco.caixaEletronico.repository;

import com.Banco.caixaEletronico.Enum.TransactionType;
import com.Banco.caixaEletronico.models.BankAccount;
import com.Banco.caixaEletronico.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM transactions t " +
                    "JOIN bank_account ba ON ba.id = t.source_account_id " +
                    "WHERE t.source_account_id = :sourceAccountId " +
                    "   AND t.transaction_type = :transactionType " +
                    "   AND t.transaction_date = CAST (GETDATE()) ")
    boolean countTransferOfSourceAccount(@Param("sourceAccountId") BankAccount sourceAccountId,@Param("transactionType") TransactionType transactionType);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM transactions t " +
                    "JOIN bank_account ba ON ba.id = t.target_account_id " +
                    "WHERE t.target_account_id = :targetAccountId " +
                    "   AND t.transaction_type = :transactionType " +
                    "   AND t.transaction_date = current_date ")
    boolean countWithdrawOfSourceAccount(@Param("targetAccountId") BankAccount targetAccountId,@Param("transactionType") TransactionType transactionType);

    @Query(nativeQuery = true,
            value = "SELECT SUM(t.transaction_value) " +
                    "FROM transactions t " +
                    "WHERE t.transaction_date = CAST (GETDATE()) " +
                    "   AND t.source_account_id = :bankAccount " +
                    "")
    BigDecimal resultOfTransactionsOfCurrentDate(@Param("bankAccount") BankAccount bankAccount);
}
