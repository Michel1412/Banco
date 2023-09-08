package com.Banco.caixaEletronico.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TransactionType {

    DEPOSIT("Deposito",1),
    TRANSFER("Transferência",2),
    WITHDRAW("Saque",3);

    private String descript;
    private Integer value;


}
