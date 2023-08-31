package com.Banco.caixaEletronico.repository;

import com.Banco.caixaEletronico.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank b " +
                    "WHERE b.bank_name = :bankName " )
    boolean countBanksByName(@Param("bankName") String bankName);

    @Query(nativeQuery = true,
            value = "SELECT bank_number " +
                    "FROM bank b " +
                    "WHERE b.bank_number = :bankNumber " )
    String findBanksByNumber(@Param("bankNumber") String bankNumber);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank b " +
                    "WHERE b.bank_number = :bankNumber " )
    boolean countByBankNumber(@Param("bankNumber") Integer bankNumber);
}
