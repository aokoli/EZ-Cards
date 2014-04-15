package com.loyola.ezcards.ezcardsapp;


public class Contact {

    public Contact(){  }

    //CB: Delete this constructor when test is complete!
    public Contact(int id, String firstName, String phoneMain){
        this.id = id;
        this.firstName = firstName;
        this.phoneMain = phoneMain;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getFirstName(){ return firstName; }

    public void setFirstName(String firstName){ this.firstName = firstName;  }

    public String getLastName(){ return lastName; }

    public void setLastName(String lastName){ this.lastName = lastName;  }

    public String getPhoneMain(){ return phoneMain; }

    public void setPhoneMain(String phoneMain){ this.phoneMain = phoneMain; }

    public String getPhoneCell(){ return phoneCell; }

    public void setPhoneCell(String phoneCell){ this.phoneCell = phoneCell; }

    public String getFax(){ return fax; }

    public void setFax(String fax){ this.fax = fax; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getCompany1(){ return company1; }

    public void setCompany1(String company1){ this.company1 = company1; }

    public String getCompany2(){ return company2; }

    public void setCompany2(String company2){ this.company2 = company2; }

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

    public String getAddress1(){ return address1; }

    public void setAddress1(String address1){ this.address1 = address1; }

    public String getAddress2(){ return address2; }

    public void setAddress2(String address2){ this.address2 = address2; }

    public String getImageLocation(){ return imageLocation; }

    public void setImageLocation(String location) { this.imageLocation = location; }

    public String getFullName(){return firstName + " " + lastName;}

    private int id;
    private String firstName;
    private String lastName;
    private String phoneMain;
    private String phoneCell;
    private String fax;
    private String email;
    private String company1;
    private String company2;
    private String title;
    private String address1;
    private String address2;
    private String imageLocation;
}
