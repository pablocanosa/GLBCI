package com.glbci.eval.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PHONES")
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

    public Phone() {
    }

    public Phone(String number, String cityCode, String countryCode, User user) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(number, phone.number) && Objects.equals(cityCode, phone.cityCode) && Objects.equals(countryCode, phone.countryCode) && Objects.equals(user, phone.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, cityCode, countryCode, user);
    }
}
