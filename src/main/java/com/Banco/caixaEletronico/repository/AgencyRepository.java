package com.Banco.caixaEletronico.repository;

import com.Banco.caixaEletronico.models.BankAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgencyRepository extends JpaRepository<BankAgency, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_agency b " +
                    "WHERE b.agency_number = :agencyNumber " )
    boolean countAgencysByNumber(@Param("agencyNumber") String agencyNumber);



    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_agency a " +
                    "JOIN bank b ON b.bank_number = a.bank " +
                    "WHERE a.agency_number = :agencyNumber " +
                    "   AND a.bank = :bankNumber ")
    boolean countAgencysByAgencyNumberAndBank(@Param("agencyNumber") Integer agencyNumber,@Param("bankNumber") Integer bankNumber);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) > 0 " +
                    "FROM bank_agency a " +
                    "JOIN bank b ON b.bank_number = a.bank " +
                    "WHERE a.bank = :bankNumber ")
    boolean countAgencysByBank(@Param("bankNumber") Integer bankNumber);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) ")
    boolean findByBank(@Param("bankNumber") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM bank_agency a " +
                    "JOIN bank b ON b.bank_number = a.bank " +
                    "WHERE a.bank = :bankNumber ")
    List<BankAgency> findAllByBank(@Param("bankNumber") Integer bankNumber);
}
