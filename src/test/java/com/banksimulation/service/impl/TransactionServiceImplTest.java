package com.banksimulation.service.impl;

import com.banksimulation.dto.AccountDTO;
import com.banksimulation.enums.AccountStatus;
import com.banksimulation.enums.AccountType;
import com.banksimulation.mapper.TransactionMapper;
import com.banksimulation.repository.TransactionRepository;
import com.banksimulation.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void should_make_transfer(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.ACTIVE,true,124L,AccountType.CHECKINGS);

        //when we call account service, I need to manually return sender and receiver
        when(accountService.retrieveById(1L)).thenReturn(sender);
        when(accountService.retrieveById(2L)).thenReturn(receiver);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNull(throwable);
    }

    private AccountDTO prepareAccountDTO(Long id, BigDecimal balance,
                                         AccountStatus accountStatus,
                                         boolean verified, Long userId,
                                         AccountType accountType){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setBalance(balance);
        accountDTO.setAccountStatus(accountStatus);
        accountDTO.setOtpVerified(verified);
        accountDTO.setUserId(userId);
        accountDTO.setAccountType(accountType);
        accountDTO.setCreationDate(new Date());
        accountDTO.setPhoneNumber("2324322233");
        return  accountDTO;
    }

}