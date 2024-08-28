package com.example.loadkaro;

public class model_signup_class {
    String Name,Contact,Email;

    public model_signup_class(String Name, String Contact,String Email) {
        this.Name = Name;
        this.Contact = Contact;
        this.Email=Email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String Contact) {
        this.Contact = Contact;
    }
}
