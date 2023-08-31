package com.Banco.caixaEletronico.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bank")
public class Bank  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bank_number")
    private Integer bankNumber;

    @Column(name = "bank_name")
    private String bankName;

}
