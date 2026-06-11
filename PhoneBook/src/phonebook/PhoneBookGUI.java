package phonebook;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneBookGUI implements PhoneBookOperations {
    
    private List<Contact> contacts;
    private final String FILE_NAME = "contacts.dat";

    private JFrame frame;
    private DefaultListModel<String> listModel;
    private JList<String> contactList;
    private JTextField searchField;

    // --- SLEEK DARK MODE PALETTE ---
    private final Color headerColor = new Color(20, 22, 30);     // Dark Navy Header
    private final Color bgColor = new Color(30, 33, 40);         // Sleek Dark Background
    private final Color btnAddColor = new Color(40, 167, 69);    // Vibrant Green
    private final Color btnEditColor = new Color(0, 123, 255);   // Vibrant Blue
    private final Color btnDeleteColor = new Color(220, 53, 69); // Vibrant Red
    private final Color btnShowColor = new Color(108, 117, 125); // Sleek Gray

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookGUI app = new PhoneBookGUI();
            app.frame.setVisible(true);
        });
    }

    public PhoneBookGUI() {
        contacts = new ArrayList<>();
        loadContactsFromFile(); 

        // If your local file is empty, automatically build your default contact list
        if (contacts.isEmpty()) {
            contacts.add(new PersonalContact("Labib Sir", "01329611192", "Teacher"));
            contacts.add(new PersonalContact("Safwan", "01300000001", "Friend"));
            contacts.add(new PersonalContact("Tamin", "01300000002", "Friend"));
            contacts.add(new PersonalContact("Sefat", "01300000003", "Friend"));
            contacts.add(new PersonalContact("Rafat", "01300000004", "Friend"));
            contacts.add(new PersonalContact("Mithila", "01300000005", "Friend"));
            contacts.add(new PersonalContact("Farhana", "01300000006", "Friend"));
            contacts.add(new PersonalContact("Sandhi", "01300000007", "Friend"));
            contacts.add(new PersonalContact("Pushon", "01300000008", "Friend"));
            contacts.add(new PersonalContact("Tasin", "01300000009", "Friend"));
            contacts.add(new PersonalContact("Samia", "01300000010", "Friend"));
            contacts.add(new PersonalContact("Nandini", "01300000011", "Friend"));
            contacts.add(new PersonalContact("Sneha", "01300000012", "Friend"));
            contacts.add(new PersonalContact("Bushra", "01300000013", "Friend"));
            
            saveContactsToFile(); 
        }
        
        initializeAppUI();
    }

    @Override
    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContactsToFile(); 
        refreshList(""); 
    }

    private void initializeAppUI() {
        // Changed window title name here
        frame = new JFrame("Phone Book"); 
        frame.setSize(450, 650); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); 
        frame.getContentPane().setBackground(bgColor);

        // ==========================================
        // 1. TOP PANEL (Header & Search Bar)
        // ==========================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 15));
        topPanel.setBackground(headerColor);
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Changed main header text here
        JLabel titleLabel = new JLabel("Phone Book"); 
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setBackground(new Color(45, 48, 55));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 110), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JButton btnSearch = createStyledButton("🔍 Search", btnShowColor);
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. MIDDLE PANEL (Sleek List Area)
        // ==========================================
        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contactList.setFixedCellHeight(55); 
        contactList.setBorder(new EmptyBorder(10, 15, 10, 15));
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        contactList.setBackground(bgColor);
        contactList.setForeground(new Color(230, 230, 230)); 
        contactList.setSelectionBackground(new Color(50, 55, 70));
        contactList.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(bgColor);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. BOTTOM PANEL (Colorful Action Dashboard)
        // ==========================================
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        bottomPanel.setBackground(bgColor);

        JButton btnAdd = createStyledButton("➕ Add Contact", btnAddColor);
        JButton btnEdit = createStyledButton("✏️ Rename / Edit", btnEditColor);
        JButton btnDelete = createStyledButton("🗑️ Delete", btnDeleteColor);
        JButton btnShowAll = createStyledButton("📋 Show All", btnShowColor);

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnShowAll);
        bottomPanel.add(btnDelete);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ==========================================
        // APP LOGIC BINDINGS
        // ==========================================

        btnSearch.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) refreshList(searchText);
        });

        btnShowAll.addActionListener(e -> {
            searchField.setText("");
            refreshList("");
        });

        btnAdd.addActionListener(e -> openAddContactDialog());

        btnEdit.addActionListener(e -> {
            String selectedItem = contactList.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(frame, "Please click a contact first!");
                return;
            }
            String selectedName = selectedItem.split("  \\|  ")[0].trim();
            openEditContactDialog(selectedName);
        });

        btnDelete.addActionListener(e -> {
            String selectedItem = contactList.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(frame, "Please click a contact first!");
                return;
            }
            
            String selectedName = selectedItem.split("  \\|  ")[0].trim();

            int confirm = JOptionPane.showConfirmDialog(frame, "Delete " + selectedName + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                contacts.removeIf(c -> c.getName().equals(selectedName));
                saveContactsToFile();
                refreshList(""); 
            }
        });

        refreshList("");
    }

    private void refreshList(String filterText) {
        listModel.clear();
        for (Contact c : contacts) {
            if (filterText.isEmpty() || c.getName().toLowerCase().contains(filterText.toLowerCase())) {
                listModel.addElement(c.getName() + "  |  📞 " + c.getPhoneNumber());
            }
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openAddContactDialog() {
        JTextField nameF = new JTextField();
        JTextField phoneF = new JTextField();
        JComboBox<String> typeF = new JComboBox<>(new String[]{"Personal", "Business"});
        JTextField extraF = new JTextField();

        Object[] message = {
            "Contact Name:", nameF,
            "Phone Number:", phoneF,
            "Type:", typeF,
            "Relation / Company:", extraF
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Create New Contact", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String n = nameF.getText().trim();
            String p = phoneF.getText().trim();
            String e = extraF.getText().trim();

            if (!n.isEmpty() && p.length() > 0) {
                if (typeF.getSelectedItem().equals("Personal")) {
                    addContact(new PersonalContact(n, p, e));
                } else {
                    addContact(new BusinessContact(n, p, e));
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Name and Phone are required!");
            }
        }
    }

    private void openEditContactDialog(String oldName) {
        Contact contactToEdit = null;
        for (Contact c : contacts) {
            if (c.getName().equals(oldName)) {
                contactToEdit = c;
                break;
            }
        }

        if (contactToEdit != null) {
            JTextField nameF = new JTextField(contactToEdit.getName());
            JTextField phoneF = new JTextField(contactToEdit.getPhoneNumber());

            Object[] message = {
                "Update Name:", nameF,
                "Update Phone:", phoneF
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Edit Contact", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String n = nameF.getText().trim();
                String p = phoneF.getText().trim();

                if (!n.isEmpty() && !p.isEmpty()) {
                    contactToEdit.setName(n);
                    contactToEdit.setPhoneNumber(p);
                    saveContactsToFile();
                    refreshList("");
                }
            }
        }
    }

    private void saveContactsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadContactsFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                contacts = (List<Contact>) ois.readObject();
            } catch (Exception e) {}
        }
    }
}