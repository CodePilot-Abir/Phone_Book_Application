package phonebook;

import java.io.Serializable;

// Abstraction & Encapsulation. Serializable allows file saving.
public abstract class Contact implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes
    
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Abstract method (Polymorphism)
    public abstract String getContactDetails();
}