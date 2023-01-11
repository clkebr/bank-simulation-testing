package com.banksimulation.service.impl;

import com.banksimulation.service.SmsService;
import com.banksimulation.dto.SmsRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public String sendSms(SmsRequestDTO smsRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            URI uri = new URI("http://localhost:8081/v1/sms");
            return restTemplate.postForObject(uri, smsRequestDTO,String.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "nok";
    }
}
