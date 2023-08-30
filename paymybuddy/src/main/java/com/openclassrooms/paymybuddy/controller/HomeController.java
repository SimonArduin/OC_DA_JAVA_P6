package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
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
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

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
    public String processRegister(UserDto user) {
        userService.addUser(user);
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
        UserDto connectedUser = userService.findByUsername(principal.getName());
        connection = userService.findById((connection.getId()));
        if(userService.addConnectionToUser(connectedUser, connection)!=null) {
            model.addAttribute("connectedUser", connectedUser);
            return "transfer";
        }
        return "error";
    }

    @GetMapping("/transfer")
    public String showAddTransactionForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        TransactionDto transaction = new TransactionDto();
        model.addAttribute("transaction", transaction);
        List<PastTransactionDto> transactionList = transactionService.getPastTransactions(connectedUser);
        model.addAttribute("transactionList", transactionList);
        return "transfer";
    }
    @PostMapping("/process_transfer")
    public String processTransfer(TransactionDto transaction, Model model, Principal principal) {
        transaction.setSenderId(userService.findByUsername(principal.getName()).getId());
        if (transactionService.addInternalTransaction(transaction) != null) {
            UserDto connectedUser = userService.findByUsername(principal.getName());
            if(connectedUser==null || connectedUser.isEmpty())
                return "error";
            model.addAttribute("connectedUser", connectedUser);
            model.addAttribute("transaction", new TransactionDto());
            List<PastTransactionDto> transactionList = transactionService.getPastTransactions(connectedUser);
            model.addAttribute("transactionList", transactionList);
            return "transfer";
        }
        return "error";
    }

    @GetMapping("/add_transaction_from_bank_account")
    public String showAddTransactionFromBankAccountForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        TransactionDto transaction = new TransactionDto();
        model.addAttribute("transaction", transaction);
        return "add_transaction_from_bank_account";
    }
    @PostMapping("/process_add_transaction_from_bank_account")
    public String processAddTransactionFromBankAccountForm(TransactionDto transaction, Model model, Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());
        if (userDto == null || userDto.isEmpty())
            return "error";
        transaction.setSenderId(userDto.getId());
        transaction.setReceiverId(userDto.getId());
        transaction.setIban(userDto.getIban());
        transaction.setToIban(false);
        if (transactionService.addExternalTransaction(transaction) != null) {
            UserDto connectedUser = userService.findByUsername(principal.getName());
            if(connectedUser==null || connectedUser.isEmpty())
                return "error";
            model.addAttribute("connectedUser", connectedUser);
            model.addAttribute("transaction", new TransactionDto());
            List<PastTransactionDto> transactionList = transactionService.getPastTransactions(connectedUser);
            model.addAttribute("transactionList", transactionList);
            return "profile";
        }
        return "error";
    }

    @GetMapping("/add_transaction_to_bank_account")
    public String showAddTransactionToBankAccountForm(Model model, Principal principal) {
        UserDto connectedUser = userService.findByUsername(principal.getName());
        if(connectedUser==null || connectedUser.isEmpty())
            return "error";
        model.addAttribute("connectedUser", connectedUser);
        TransactionDto transaction = new TransactionDto();
        model.addAttribute("transaction", transaction);
        return "add_transaction_to_bank_account";
    }
    @PostMapping("/process_add_transaction_to_bank_account")
    public String processAddTransactionToBankAccountForm(TransactionDto transaction, Model model, Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());
        if (userDto == null || userDto.isEmpty())
            return "error";
        transaction.setSenderId(userDto.getId());
        transaction.setReceiverId(userDto.getId());
        transaction.setIban(userDto.getIban());
        transaction.setToIban(true);
        if (transactionService.addExternalTransaction(transaction) != null) {
            UserDto connectedUser = userService.findByUsername(principal.getName());
            if(connectedUser==null || connectedUser.isEmpty())
                return "error";
            model.addAttribute("connectedUser", connectedUser);
            model.addAttribute("transaction", new TransactionDto());
            List<PastTransactionDto> transactionList = transactionService.getPastTransactions(connectedUser);
            model.addAttribute("transactionList", transactionList);
            return "profile";
        }
        return "error";
    }
}
