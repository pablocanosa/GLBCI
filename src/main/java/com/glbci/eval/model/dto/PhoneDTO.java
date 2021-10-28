package com.glbci.eval.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhoneDTO {

    private String number;
    private String citycode;
    private String countrycode;

}
