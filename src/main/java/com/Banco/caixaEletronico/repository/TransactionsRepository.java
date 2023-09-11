package com.Banco.caixaEletronico.repository;

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
                    "   AND t.transaction_date > current_date ")
    boolean countTransferOfSourceAccount(@Param("sourceAccountId") Integer sourceAccountId,@Param("transactionType") String transactionType);

    @Query (nativeQuery = true,
            value = "SELECT COALESCE(SUM(transaction_value),0) " +
                    "FROM transactions t " +
                    "WHERE t.transaction_date > current_date " +
                    "AND t.source_account_id = :bankAccount")
    BigDecimal sumTransferOfSourceAccount(@Param("bankAccount") Integer bankAccount);
    @Query (nativeQuery = true,

            value = "SELECT COALESCE(SUM(t.transaction_value),0) " +
                    "FROM transactions t " +
                    "WHERE t.transaction_date > current_date " +
                    "AND t.target_account_id = :bankAccount")
    BigDecimal sumTransferOfTargetAccount(@Param("bankAccount") Integer bankAccount);
}
