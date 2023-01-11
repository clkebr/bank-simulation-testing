package com.banksimulation.service;

import com.banksimulation.dto.OtpDTO;
import com.banksimulation.entity.Account;

public interface OtpService {
    OtpDTO createOtpSendSms(Account account);

    void confirmOtp(Integer otpCode, Long otpId);
}
