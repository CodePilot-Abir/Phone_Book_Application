package phonebook;

public interface PhoneBookOperations {
    void addContact(Contact contact);
    // In a GUI, displaying and searching are usually handled by the UI components,
    // but we keep this interface to demonstrate Abstraction.
}