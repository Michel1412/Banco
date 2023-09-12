package com.Banco.caixaEletronico.repository;


import com.Banco.caixaEletronico.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccontRepository extends JpaRepository<BankAccount, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_account ba " +
                    "JOIN bank_agency ba2 ON ba2.id = ba.agency " +
                    "WHERE ba.account_number = :accountNumber " +
                    "   AND ba.agency = :agencyId ")
    boolean countAccountByAccountNumber(@Param("accountNumber") Integer accountNumber,@Param("agencyId") Integer agencyId);
    @Query(nativeQuery = true,
            value = "SELECT count(*) > 0  " +
                    "FROM bank_agency ba " +
                    "JOIN bank_account ba2 ON ba2.agency = ba.id " +
                    "WHERE ba2.associate_id = :associateId AND ba.bank = ( " +
                    " SELECT ba.bank " +
                    " FROM bank_agency ba " +
                    " JOIN bank_account ba2 ON ba.id = ba2.agency " +
                    " WHERE ba.id = :agencyId AND ba2.associate_id = :associateId)")
    boolean countAccountOnAgencyByBankAndAssociateId(@Param("associateId")Integer associateId, @Param("agencyId") Integer agencyId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_account ba " +
                    "JOIN bank_agency ba2 ON ba.agency = ba2.id " +
                    "WHERE ba.agency = :agencyId ")
    boolean countAllByAgency(@Param("agencyId") Integer agencyId);

    @Query(nativeQuery = true,
            value = "SELECT ba.balance " +
                    "FROM bank_account ba " +
                    "WHERE ba.id = :bankAccount")
    BigDecimal findBalanceById(@Param("bankAccount") BankAccount bankAccount);
}
