package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import com.openclassrooms.paymybuddy.dto.InternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.service.GlobalService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GlobalService globalService;

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(UserDto userDto) {
        if (userDto == null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        userService.addUser(userDto);
        return "register_success";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        return "profile";
    }

    @GetMapping("/contact")
    public String contact(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        return "contact";
    }

    @GetMapping("/add_connection")
    public String showAddConnectionForm(Model model) {
        UserDto connection = new UserDto();
        model.addAttribute("connection", connection);
        return "add_connection_form";
    }
    @PostMapping("/process_add_connection")
    public String processAddConnection(UserDto connection, Model model, Principal principal) {
        if (model == null || principal == null)
            return "error";
        if (connection == null || connection.getUsername() == null)
            throw new IllegalArgumentException("Invalid connection to add");
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if (connectedUser == null || connectedUser.isEmpty())
            return "error";
        connection = userService.findByUsername((connection.getUsername()));
        if (connection == null || connection.isEmpty())
            return "error";
        if(userService.addConnectionToUser(connectedUser, connection)!=null) {
            model.addAttribute("connectedUser", connectedUser);
            return contact(model, principal);
        }
        return "error";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        InternalTransactionDto transaction = new InternalTransactionDto();
        model.addAttribute("transaction", transaction);
        List<PastTransactionDto> transactionList = globalService.getPastTransactions(connectedUser);
        model.addAttribute("transactionList", transactionList);
        return "transfer";
    }
    @PostMapping("/process_transfer")
    public String processTransfer(InternalTransactionDto internalTransactionDto, Model model, Principal principal) {
        if (model == null || principal == null)
            return "error";
        if(internalTransactionDto == null)
            throw new IllegalArgumentException("Invalid transaction");
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if (connectedUser == null || connectedUser.isEmpty())
            return "error";
        internalTransactionDto.setSenderId(connectedUser.getId());
        if (internalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");
        if (globalService.addInternalTransaction(internalTransactionDto) != null) {
            model.addAttribute("connectedUser", connectedUser);
            model.addAttribute("transaction", new InternalTransactionDto());
            List<PastTransactionDto> transactionList = globalService.getPastTransactions(connectedUser);
            model.addAttribute("transactionList", transactionList);
            return showTransferForm(model, principal);
        }
        return "error";
    }

    @GetMapping("/add_transaction_from_bank_account")
    public String showAddTransactionFromBankAccountForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        ExternalTransactionDto transaction = new ExternalTransactionDto();
        model.addAttribute("transaction", transaction);
        return "add_transaction_from_bank_account";
    }
    @PostMapping("/process_add_transaction_from_bank_account")
    public String processAddTransactionFromBankAccountForm(ExternalTransactionDto externalTransactionDto, Model model, Principal principal) {
        if (model == null || principal == null)
            return "error";
        if (externalTransactionDto == null)
            throw new IllegalArgumentException("Invalid transaction");
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if (connectedUser == null || connectedUser.isEmpty())
            return "error";
        externalTransactionDto.setSenderId(connectedUser.getId());
        externalTransactionDto.setIban(connectedUser.getIban());
        externalTransactionDto.setToIban(false);
        if (externalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");
        if (globalService.addExternalTransaction(externalTransactionDto) != null)
            return profile(model, principal);
        return "error";
    }

    @GetMapping("/add_transaction_to_bank_account")
    public String showAddTransactionToBankAccountForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        ExternalTransactionDto transaction = new ExternalTransactionDto();
        model.addAttribute("transaction", transaction);
        return "add_transaction_to_bank_account";
    }
    @PostMapping("/process_add_transaction_to_bank_account")
    public String processAddTransactionToBankAccountForm(ExternalTransactionDto externalTransactionDto, Model model, Principal principal) {
        if (model == null || principal == null)
            return "error";
        if (externalTransactionDto == null)
            throw new IllegalArgumentException("Invalid transaction");
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if (connectedUser == null || connectedUser.isEmpty())
            return "error";
        externalTransactionDto.setSenderId(connectedUser.getId());
        externalTransactionDto.setIban(connectedUser.getIban());
        externalTransactionDto.setToIban(true);
        if (externalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");
        if (globalService.addExternalTransaction(externalTransactionDto) != null)
            return profile(model, principal);
        return "error";
    }
}
