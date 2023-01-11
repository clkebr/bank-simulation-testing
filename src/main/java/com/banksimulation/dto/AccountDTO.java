package com.banksimulation.dto;

import com.banksimulation.enums.AccountStatus;
import com.banksimulation.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private Date creationDate;
    private Long userId;
    private BigDecimal balance;
    private Boolean otpVerified;
    private String phoneNumber;

}
