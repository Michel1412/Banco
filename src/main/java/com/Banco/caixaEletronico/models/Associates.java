package com.Banco.caixaEletronico.models;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "associates")
public class Associates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "salary")
    private BigDecimal salary;

}


