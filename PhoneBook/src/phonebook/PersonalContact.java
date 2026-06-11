package phonebook;

// Inheritance
public class PersonalContact extends Contact {
    private static final long serialVersionUID = 1L;
    private String relationship;

    public PersonalContact(String name, String phoneNumber, String relationship) {
        super(name, phoneNumber);
        this.relationship = relationship;
    }

    // Polymorphism
    @Override
    public String getContactDetails() {
        return "[Personal] Name: " + getName() + " | Phone: " + getPhoneNumber() + " | Relation: " + relationship;
    }
}