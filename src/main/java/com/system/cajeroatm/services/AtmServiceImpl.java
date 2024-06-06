package com.system.cajeroatm.services;

import com.system.cajeroatm.exception.AmountNotAllowed;
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
        availableMoney.add(new Money(BILL,2, 1000.0));
        availableMoney.add(new Money(BILL,5, 500.0));
        availableMoney.add(new Money(BILL,10, 200.0));
        availableMoney.add(new Money(BILL,20, 100.0));
        availableMoney.add(new Money(BILL,30, 50.0));
        availableMoney.add(new Money(BILL,40, 20.0));
        availableMoney.add(new Money(COIN,50, 10.0));
        availableMoney.add(new Money(COIN,100, 5.0));
        availableMoney.add(new Money(COIN,200, 2.0));
        availableMoney.add(new Money(COIN,300, 1.0));
        availableMoney.add(new Money(COIN,100, 0.5));
    }


    @Override
    public List<Money> calculateWithdrawal(Double amount) throws InsufficientFundsException, AmountNotAllowed{
        List<Money> withdrawal = new ArrayList<>();
        double fractionalPart = amount % 1;
        if (amount < 0) {
            throw new AmountNotAllowed("El monto ingresado no puede ser menor a 0");
        }else if (fractionalPart > 0.5 || (fractionalPart < 0.5 && fractionalPart > 0)) {
            throw new AmountNotAllowed("El monto ingresado no puede tener decimales más que .5");
        }else{
            double originalAmount = amount;
            for (Money money : availableMoney) {
                int neededCant = (int) (amount / money.getDenomination());
                if (neededCant > 0) {
                    int withdrawnQuantity = Math.min(neededCant, money.getCant());
                    if (withdrawnQuantity > 0) {
                        withdrawal.add(new Money(money.getType(), withdrawnQuantity, money.getDenomination()));
                        amount -= withdrawnQuantity * money.getDenomination();
                        money.setCant(money.getCant() - withdrawnQuantity);
                    }
                }
            }
            if (amount > 0) {
                throw new InsufficientFundsException("No hay suficiente dinero para retirar " + originalAmount + " pesos.");
            }
        }



        return withdrawal;
    }

    @Override
    public List<Money> getAvailableMoney() {
        return availableMoney;
    }

    @Override
    public double calculateTotalWithdrawn(List<Money> withdrawal) {
        return withdrawal.stream()
                .mapToDouble(money -> money.getDenomination() * money.getCant())
                .sum();
    }

    @Override
    public double calculateTotalAvailable() {
        return availableMoney.stream()
                .mapToDouble(money -> money.getDenomination() * money.getCant())
                .sum();
    }
}
