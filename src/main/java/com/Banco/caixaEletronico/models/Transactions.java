package com.Banco.caixaEletronico.models;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_value")
    private BigDecimal transactionValue;

    @Column(name = "transaction_date")
    private Date transactionDate;//dia e horario

    @OneToOne
    @JoinColumn(name = "source_account_id")
    private BankAccount sourceAccountId;//conta origem

    @OneToOne
    @JoinColumn(name = "target_account_id")
    private BankAccount targetAccountId;//conta destino

    @Column(name = "status")
    private String status;
}