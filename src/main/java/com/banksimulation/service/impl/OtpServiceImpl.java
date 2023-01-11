package com.banksimulation.service.impl;

import com.banksimulation.entity.Account;
import com.banksimulation.service.SmsService;
import com.banksimulation.dto.OtpDTO;
import com.banksimulation.dto.SmsRequestDTO;
import com.banksimulation.entity.Otp;
import com.banksimulation.enums.VerificationStatus;
import com.banksimulation.exception.OtpInvalidException;
import com.banksimulation.exception.SmsException;
import com.banksimulation.repository.AccountRepository;
import com.banksimulation.repository.OtpRepository;
import com.banksimulation.service.OtpService;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final AccountRepository accountRepository;
    private final SmsService smsService;

    public OtpServiceImpl(OtpRepository otpRepository,
                          AccountRepository accountRepository,
                          SmsService smsService) {
        this.otpRepository = otpRepository;
        this.accountRepository = accountRepository;
        this.smsService = smsService;
    }

    @Override
    public OtpDTO createOtpSendSms(Account account) {
        Otp otp = new Otp();
        otp.setAccount(account);
        otp.setVerificationStatus(VerificationStatus.PENDING);
        otp.setOtpCode(new Random().nextInt(900000) + 100000);
        otpRepository.save(otp);

        SmsRequestDTO smsRequestDTO = new SmsRequestDTO(account.getPhoneNumber(), prepareMessage(otp.getOtpCode()));

        String smsResponse = smsService.sendSms(smsRequestDTO);
        if (!smsResponse.equals("ok")){
            throw new SmsException("sms couldn't send");
        }

        return new OtpDTO(otp.getOtpCode(), otp.getId());
    }

    @Override
    public void confirmOtp(Integer otpCode, Long otpId) {
        Otp otp = otpRepository.getById(otpId);
        if (otp == null){
            throw new OtpInvalidException("otp not found");
        }

        if (otp.getOtpCode().equals(otpCode)){
            Account account = otp.getAccount();
            account.setOtpVerified(true);
            accountRepository.save(account);
        }else {
            throw new OtpInvalidException("Otpcode does not match");
        }

    }

    public String prepareMessage(Integer otpCode){
        return "To verify account please use " + otpCode + "otpCode";
    }
}
