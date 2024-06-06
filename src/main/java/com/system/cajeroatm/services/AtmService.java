package com.system.cajeroatm.services;

import com.system.cajeroatm.exception.InsufficientFundsException;
import com.system.cajeroatm.model.Money;

import java.util.List;

public interface AtmService {
    List<Money> calculateWithdrawal(Double amount) throws InsufficientFundsException;
    List<Money> getAvailableMoney();
}
