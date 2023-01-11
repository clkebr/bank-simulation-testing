package com.banksimulation.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {
    public boolean success;
    public String message;
    public int code;
    public OtpDTO data;
}


