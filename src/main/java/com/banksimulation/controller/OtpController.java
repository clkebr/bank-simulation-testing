package com.banksimulation.controller;

import com.banksimulation.dto.OtpDTO;
import com.banksimulation.dto.ResponseWrapper;
import com.banksimulation.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/otp")
public class OtpController {
    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> confirmOtp(@RequestBody OtpDTO otpDTO){
        otpService.confirmOtp(otpDTO.getOtpCode(), otpDTO.getOtpId());
        return ResponseEntity.ok(new ResponseWrapper("Otp is successfully confirmed ", HttpStatus.OK));
    }
}
