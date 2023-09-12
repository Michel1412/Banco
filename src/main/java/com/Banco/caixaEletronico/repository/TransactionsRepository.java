package com.Banco.caixaEletronico.repository;

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
                    "   AND t.transaction_date > current_date " +
                    "   AND t.status = 'aprovado'")
    boolean countTransactionsOfSourceAccount(@Param("sourceAccountId") Integer sourceAccountId, @Param("transactionType") String transactionType);

    @Query (nativeQuery = true,
            value = "SELECT COALESCE(SUM(transaction_value),0) " +
                    "FROM transactions t " +
                    "WHERE t.transaction_date > current_date " +
                    "AND t.source_account_id = :bankAccount " +
                    "AND t.transaction_type != 'Estorno' " +
                    "AND t.status = 'aprovado'")
    BigDecimal sumTransferOfSourceAccount(@Param("bankAccount") Integer bankAccount);
    @Query (nativeQuery = true,
            value = "SELECT COALESCE(SUM(t.transaction_value),0) " +
                    "FROM transactions t " +
                    "WHERE t.transaction_date > current_date " +
                    "AND t.transaction_type != 'Estorno' " +
                    "AND t.target_account_id = :bankAccount " +
                    "AND t.status = 'aprovado'")
    BigDecimal sumTransferOfTargetAccount(@Param("bankAccount") Integer bankAccount);

    @Query(nativeQuery = true,
            value = "SELECT full_balance_transaction " +
                    "FROM bank b JOIN bank_agency ba " +
                    " ON b.bank_number = ba.bank join bank_account ba2 " +
                    " on ba2.agency = ba.id " +
                    " WHERE ba2.id = :sourceAccountId")
    BigDecimal findFullBalnceByAccountId(@Param("sourceAccountId") Integer sourceAccountId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM transactions t " +
                    "WHERE t.source_account_id = :sourceAccountId " +
                    "AND t.target_account_id = :targetAccountId " +
                    "AND t.transaction_value = :transactionValue " +
                    "AND t.transaction_type = 'Estorno' ")
    boolean countReversalTransferByTargetAndSourceId(@Param("targetAccountId") BankAccount targetAccountId,
                                                     @Param("sourceAccountId") BankAccount sourceAccountId,
                                                     @Param("transactionValue") BigDecimal transactionValue);
}
