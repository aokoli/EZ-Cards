package com.loyola.ezcards.ezcardsapp;

/**
 * Created by iraziud on 4/11/14.
 */
import java.util.Arrays;

public class BusinessCard {
    private String company;
    private String firstName;
    private String lastName;
    private String title;
    private String address;
    private String[] phones;
    private String fax;
    private String email;

    public BusinessCard() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getPhones() {
        return phones;
    }

    public void setPhones(String[] phones) {
        this.phones = phones;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "BusinessCard [company=" + company
                + "; firstName=" + firstName + "; lastName=" + lastName
                + "; title=" + title + "; address=" + address
                + "; phones=" + Arrays.toString(phones) + "; fax=" + fax
                + "; email=" + email + "]";
    }
}
