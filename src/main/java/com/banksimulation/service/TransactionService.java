package com.banksimulation.service;

import com.banksimulation.dto.AccountDTO;
import com.banksimulation.dto.TransactionDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionService {

    TransactionDTO makeTransfer(BigDecimal amount, Date creationDate, AccountDTO sender, AccountDTO receiver, String message);

    List<TransactionDTO> findAll();

    List<TransactionDTO> retrieveLastTransactions();

    List<TransactionDTO> findTransactionListByAccountId(Long id);

}
