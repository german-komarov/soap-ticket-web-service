package com.german.soapwebservice.util.validators;

import com.german.soapwebservice.exceptions.BankCardCredentialsNotValidException;
import localhost._8080.tickets.BankCardCredentialsDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class BankCardValidator {

    public void validate(BankCardCredentialsDto bankCardCredentialsDto) throws BankCardCredentialsNotValidException {

        LocalDate expirationDate;
        try {
            Long.parseLong(bankCardCredentialsDto.getCardNumber());
            Integer.parseInt(bankCardCredentialsDto.getCvv());


            String[] splittedDate = bankCardCredentialsDto.getExpirationDate().split("/");

            int year = Integer.parseInt("20" + splittedDate[1]);
            int month = Integer.parseInt(splittedDate[0]);


            expirationDate = LocalDate.of(year, month, 1);
        } catch (Exception e) {
            throw new BankCardCredentialsNotValidException(e.getMessage());
        }


        if (expirationDate.isBefore(LocalDate.now())) {

            throw new BankCardCredentialsNotValidException("You bank card has been expired");
        }


    }

}
