package com.Banco.caixaEletronico.models;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bank_agency")
public class BankAgency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bank")
    private Bank bankNumber;

    @Column(name = "agency_number")
    private long agencyNumber;

}
