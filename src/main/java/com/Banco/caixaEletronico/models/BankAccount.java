package com.Banco.caixaEletronico.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "account_number")
    private Integer accountNumber;

    @ManyToOne
    @JoinColumn(name = "bank_number")
    private BankAgency bankNumber;

    @ManyToOne
    @JoinColumn(name = "agency_number")
    private BankAgency agencyNumber;

    @ManyToOne
    @JoinColumn(name = "associate_id")
    private Associates associateId;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "transaction_limit")
    private Associates transactionLimit;
}



