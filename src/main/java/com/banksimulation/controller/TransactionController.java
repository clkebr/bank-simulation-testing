package com.banksimulation.controller;

import com.banksimulation.service.AccountService;
import com.banksimulation.dto.AccountDTO;
import com.banksimulation.dto.ResponseWrapper;
import com.banksimulation.dto.TransactionDTO;
import com.banksimulation.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Date;

@Controller
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseWrapper> makeTransfer(TransactionDTO transactionDTO) {
        AccountDTO reciever = transactionDTO.getReceiver();
        AccountDTO sender = transactionDTO.getSender();
        transactionService.makeTransfer(transactionDTO.getAmount(), new Date(), sender, reciever, transactionDTO.getMessage());
        return ResponseEntity.ok(new ResponseWrapper("Transaction is successfully made", HttpStatus.OK));

    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<ResponseWrapper> transactionDetailById(Long accountId) {
        return ResponseEntity.ok(new ResponseWrapper("Transaction is successfully made",
                transactionService.findTransactionListByAccountId(accountId), HttpStatus.OK));
    }

}
