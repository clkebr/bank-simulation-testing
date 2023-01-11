package com.banksimulation.service.impl;

import com.banksimulation.dto.AccountDTO;
import com.banksimulation.enums.AccountStatus;
import com.banksimulation.enums.AccountType;
import com.banksimulation.exception.AccountNotVerifiedException;
import com.banksimulation.exception.AccountOwnerShipException;
import com.banksimulation.exception.BadRequestException;
import com.banksimulation.exception.BalanceNotSufficientException;
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
    @Test
    public void should_throw_bad_request_exception_when_sender_account_is_null(){
        AccountDTO sender = null;
        AccountDTO receiver = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.ACTIVE,true,124L,AccountType.CHECKINGS);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);

        assertInstanceOf(BadRequestException.class,throwable);

        assertEquals("Sender or receiver can not be null",((BadRequestException)throwable).getMessage());
    }
    @Test
    public void should_throw_bad_request_exception_when_receiver_account_is_null(){
        AccountDTO receiver = null;
        AccountDTO sender = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.ACTIVE,true,124L,AccountType.CHECKINGS);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);

        assertInstanceOf(BadRequestException.class,throwable);

        assertEquals("Sender or receiver can not be null",((BadRequestException)throwable).getMessage());
    }
    @Test
    public void should_throw_bad_request_exception_when_sender_and_receiver_id_is_same(){
        AccountDTO sender = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.ACTIVE,true,124L,AccountType.CHECKINGS);


        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(BadRequestException.class,throwable);
        assertEquals("Sender account needs to be different from receiver account",((BadRequestException)throwable).getMessage());

    }
    @Test
    public void should_throw_bad_request_exception_when_sender_account_status_is_deleted(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.DELETED, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.ACTIVE,true,124L,AccountType.CHECKINGS);


        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(BadRequestException.class,throwable);
        assertEquals("Sender account is deleted, you can not send money from this account",((BadRequestException)throwable).getMessage());

    }
    @Test
    public void should_throw_bad_request_exception_when_receiver_account_status_is_deleted(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L,new BigDecimal(150),AccountStatus.DELETED,true,124L,AccountType.CHECKINGS);


        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(BadRequestException.class,throwable);
        assertEquals("Receiver account is deleted, you can not send money to this account",((BadRequestException)throwable).getMessage());

    }
    @Test
    public void should_throw_account_not_verified_exception_when_sender_account_is_not_verified(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, false, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.CHECKINGS);

        when(accountService.retrieveById(1L)).thenReturn(sender);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(AccountNotVerifiedException.class,throwable);
        assertEquals("account not verified yet.",((AccountNotVerifiedException)throwable).getMessage());

    }
    @Test
    public void should_throw_account_not_verified_exception_when_receiver_account_is_not_verified(){
        AccountDTO receiver = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, false, 123L, AccountType.CHECKINGS);
        AccountDTO sender = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.CHECKINGS);

        when(accountService.retrieveById(2L)).thenReturn(sender);
        when(accountService.retrieveById(1L)).thenReturn(receiver);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(AccountNotVerifiedException.class,throwable);
        assertEquals("account not verified yet.",((AccountNotVerifiedException)throwable).getMessage());
    }
    @Test
    public void should_throw_account_ownership_exception_when_sender_account_is_saving_but_user_id_is_different(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.SAVINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.CHECKINGS);

        when(accountService.retrieveById(1L)).thenReturn(sender);
        when(accountService.retrieveById(2L)).thenReturn(receiver);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(AccountOwnerShipException.class,throwable);
        assertEquals("When one of the account type is SAVINGS, sender and receiver has to be same person",((AccountOwnerShipException)throwable).getMessage());

    }
    @Test
    public void should_throw_account_ownership_exception_when_receiver_account_is_saving_but_user_id_is_different(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.SAVINGS);

        when(accountService.retrieveById(1L)).thenReturn(sender);
        when(accountService.retrieveById(2L)).thenReturn(receiver);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(AccountOwnerShipException.class,throwable);
        assertEquals("When one of the account type is SAVINGS, sender and receiver has to be same person",((AccountOwnerShipException)throwable).getMessage());

    }
    @Test
    public void should_work_when_sender_and_receiver_account_are_saving_and_user_id_is_same(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.SAVINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 123L, AccountType.SAVINGS);

        when(accountService.retrieveById(1L)).thenReturn(sender);
        when(accountService.retrieveById(2L)).thenReturn(receiver);

        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNull(throwable);

    }

    @Test
    public void should_throw_balance_no_sufficient_exception_when_sender_balance_is_not_enough(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(5), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.CHECKINGS);

        when(accountService.retrieveById(1L)).thenReturn(sender);
        when(accountService.retrieveById(2L)).thenReturn(receiver);


        Throwable throwable = catchThrowable(() -> transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "some message"));

        assertNotNull(throwable);
        assertInstanceOf(BalanceNotSufficientException.class,throwable);
        assertEquals("Balance is not enough for this transaction",((BalanceNotSufficientException)throwable).getMessage());

    }
    @Test
    public void should_make_transfer_when_sender_balance_is_equal_to_amount(){
        AccountDTO sender = prepareAccountDTO(1L, new BigDecimal(10), AccountStatus.ACTIVE, true, 123L, AccountType.CHECKINGS);
        AccountDTO receiver = prepareAccountDTO(2L, new BigDecimal(250), AccountStatus.ACTIVE, true, 124L, AccountType.CHECKINGS);

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