package com.system.cajeroatm.controller;

import com.system.cajeroatm.enums.MoneyT;
import com.system.cajeroatm.exception.AmountNotAllowed;
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
    private final AtmService atmService;

    @Autowired
    public AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/")
    public String showAtm(Model model) {
        double totalAvailable = atmService.calculateTotalAvailable();
        model.addAttribute("availableMoney", atmService.getAvailableMoney());
        model.addAttribute("totalAvailable", totalAvailable);
        return "atm";
    }

    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestParam double amount, Model model) {
        try {
            List<Money> withdrawal = atmService.calculateWithdrawal(amount);
            double totalWithdrawn = atmService.calculateTotalWithdrawn(withdrawal);
            double totalAvailable = atmService.calculateTotalAvailable();
            model.addAttribute("withdrawal", withdrawal);
            model.addAttribute("totalWithdrawn", totalWithdrawn);
            model.addAttribute("totalAvailable", totalAvailable);
            model.addAttribute("requestedAmount", amount);
            model.addAttribute("availableMoney", atmService.getAvailableMoney());
        } catch (InsufficientFundsException | AmountNotAllowed e) {
            model.addAttribute("error", e.getMessage());
            return showAtm(model);
        }
        return "atm";
    }
}
