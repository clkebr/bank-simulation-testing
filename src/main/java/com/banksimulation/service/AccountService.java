package com.banksimulation.service;

import com.banksimulation.dto.AccountDTO;
import com.banksimulation.dto.OtpDTO;

import java.util.List;

public interface AccountService {

    OtpDTO createNewAccount(AccountDTO accountDTO);

    List<AccountDTO> listAllAccount();

    List<AccountDTO> listAllActiveAccount();

    AccountDTO deleteAccount(Long account);

    AccountDTO retrieveById(Long account);

}
