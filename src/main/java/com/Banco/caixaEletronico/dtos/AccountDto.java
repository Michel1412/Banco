package com.Banco.caixaEletronico.dtos;

import com.Banco.caixaEletronico.models.Associates;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class AccountDto {

    @NotNull
    private Integer accountNumber;

    @NotNull
    private Integer agencyId;

    @NotNull
    private Integer associateId;

    private BigDecimal balance;

    private BigDecimal transactionLimit;
}
