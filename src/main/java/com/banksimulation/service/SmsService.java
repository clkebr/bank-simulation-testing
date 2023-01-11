package com.banksimulation.service;

import com.banksimulation.dto.SmsRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    String sendSms(SmsRequestDTO smsRequestDTO);

}
