package com.system.cajeroatm.controller;

import com.system.cajeroatm.enums.MoneyT;
import com.system.cajeroatm.exception.InsufficientFundsException;
import com.system.cajeroatm.model.Money;
import com.system.cajeroatm.services.AtmService;
import com.system.cajeroatm.services.AtmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AtmController {
    private final AtmServiceImpl atmServiceImpl;

    @Autowired
    public AtmController(AtmServiceImpl atmServiceImpl) {
        this.atmServiceImpl = atmServiceImpl;
    }

    @GetMapping("/")
    public String showAtm(Model model) {
        model.addAttribute("moneyT", MoneyT.class);
        model.addAttribute("availableMoney", atmServiceImpl.getAvailableMoney());
        return "atm";
    }

    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestParam double amount, Model model) {
        try {
            List<Money> withdrawal = atmServiceImpl.calculateWithdrawal(amount);
            model.addAttribute("withdrawal", withdrawal);
            model.addAttribute("requestedAmount", amount);
        } catch (InsufficientFundsException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "atm";
    }
}
