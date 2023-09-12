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
    @JoinColumn(name = "agency")
    private BankAgency agencyId;

    @OneToOne
    @JoinColumn(name = "associate_id")
    private Associates associateId;

    @Column(name = "balance")
    private BigDecimal balance;

}



