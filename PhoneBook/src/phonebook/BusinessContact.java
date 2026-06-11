package phonebook;

// Inheritance
public class BusinessContact extends Contact {
    private static final long serialVersionUID = 1L;
    private String companyName;

    public BusinessContact(String name, String phoneNumber, String companyName) {
        super(name, phoneNumber);
        this.companyName = companyName;
    }

    // Polymorphism
    @Override
    public String getContactDetails() {
        return "[Business] Name: " + getName() + " | Phone: " + getPhoneNumber() + " | Company: " + companyName;
    }
}