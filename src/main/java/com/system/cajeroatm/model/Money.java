package com.system.cajeroatm.model;

import com.system.cajeroatm.enums.MoneyT;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Money {

    public MoneyT type;
    public int cant;
    public Double denomination;


    public String getDisplayText() {
        return (type == MoneyT.COIN ? "monedas" : "billetes");
    }

}
