package com.Banco.caixaEletronico.dtos;

import com.Banco.caixaEletronico.models.BankAccount;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionDto {


    private String transactionType;

    @NotNull
    private BigDecimal transactionValue;

    private Date transactionDate;//dia e horario

    private Integer sourceAccountId;//conta origem

    private Integer targetAccountId;//conta destino

}
