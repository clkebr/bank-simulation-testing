package com.banksimulation.mapper;

import com.banksimulation.dto.AccountDTO;
import com.banksimulation.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Account convertToEntity(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }

    public AccountDTO convertToDto(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

}
