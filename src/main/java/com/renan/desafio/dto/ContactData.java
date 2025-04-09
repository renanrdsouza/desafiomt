package com.renan.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactData {
    private Properties properties;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        private String email;
        private String firstname;
        private String lastname;
        private String phone;
        private String company;
    }
}
