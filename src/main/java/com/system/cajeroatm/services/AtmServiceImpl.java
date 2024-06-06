package com.system.cajeroatm.services;

import com.system.cajeroatm.exception.InsufficientFundsException;
import com.system.cajeroatm.model.Money;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.system.cajeroatm.enums.MoneyT.*;

@Service
public class AtmServiceImpl implements AtmService{
    private final List<Money> availableMoney;

    public AtmServiceImpl() {
        availableMoney = new ArrayList<>();
        initializeMoney();
    }
    private void initializeMoney() {
        availableMoney.add(new Money(COIN,2, 1000.0));
        availableMoney.add(new Money(COIN,5, 500.0));
        availableMoney.add(new Money(COIN,10, 200.0));
        availableMoney.add(new Money(BILL,20, 100.0));
        availableMoney.add(new Money(BILL,30, 50.0));
        availableMoney.add(new Money(BILL,40, 20.0));
        availableMoney.add(new Money(BILL,50, 10.0));
        availableMoney.add(new Money(BILL,100, 5.0));
        availableMoney.add(new Money(BILL,200, 2.0));
        availableMoney.add(new Money(BILL,300, 1.0));
        availableMoney.add(new Money(BILL,100, 0.5));
    }


    @Override
    public List<Money> calculateWithdrawal(Double amount) throws InsufficientFundsException {
        List<Money> withdrawal = new ArrayList<>();
        double originalAmount = amount;
        for (Money money : availableMoney) {
            int neededCant = (int) (amount / money.getDenomination());
            if (neededCant > 0) {
                int withdrawnQuantity = Math.min(neededCant, money.getCant());
                if (withdrawnQuantity > 0) {
                    withdrawal.add(new Money(money.getType(), withdrawnQuantity, money.getDenomination()));
                    amount -= withdrawnQuantity * money.getDenomination();
                }
            }
        }
        if (amount > 0) {
            throw new InsufficientFundsException("No hay suficiente dinero para retirar " + originalAmount + " pesos.");
        }
        return withdrawal;
    }

    @Override
    public List<Money> getAvailableMoney() {
        return availableMoney;
    }
}
