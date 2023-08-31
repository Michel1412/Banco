package com.Banco.caixaEletronico.dtos;


import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class AgencyDto {

    @NotNull
    private Integer bankNumber;

    @NotNull
    private Integer agencyNumber;

}
