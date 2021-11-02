package com.glbci.eval.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PHONES")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Phone {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "PHONES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PHONE_NUMBER")
    private String number;

    @Column(name = "CITY_CODE")
    private String cityCode;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Phone(String number, String cityCode, String countryCode, User user) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
        this.user = user;
    }

}
