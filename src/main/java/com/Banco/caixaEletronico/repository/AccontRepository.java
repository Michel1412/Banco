package com.Banco.caixaEletronico.repository;


import com.Banco.caixaEletronico.dtos.AccountDto;
import com.Banco.caixaEletronico.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccontRepository extends JpaRepository<BankAccount, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_account ba " +
                    "WHERE ba.account_number = :accountNumber")
    boolean countAccountByAccountNumber(@Param("account_number") Integer accountNumber);


    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_agency ba " +
                    "WHERE ba.id = :agencyId")
    boolean validateAgencyExistsById(@Param("id") Integer agencyId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM associates a " +
                    "WHERE a.id = :associateId ")
    boolean validadeAssociateById(@Param("id") Integer associateId);
}
