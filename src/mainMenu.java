import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class mainMenu extends JFrame {
    private Container c;
    private JTabbedPane tabbedPane1;
    private JTextField addMemberFirstNameField;
    private JButton addMemberAddButton;
    private JTextArea addMemberLogField;
    private JTextField addMemberLastNameField;
    private JTextField addMemberPhoneNumField;
    private JPanel mainPanel;
    private JLabel staffNameField;
    private JTextField addStaffFirstNameField;
    private JButton addStaffButton;
    private JTextPane addStaffLog;
    private JTextField addStaffLastNameField;
    private JTextField addStaffPhoneNumField;
    private JTextField addStaffJobTitleField;
    private JTextField addStaffUernameField;
    private JPasswordField addStaffPasswordField;
    private JPasswordField addStaffVerifyPasswordField;
    private JTextArea addEquipmentLog;
    private JButton addEquipmentButton;
    private JRadioButton addEquipmentSellableButton;
    private JTextField addEquipmentCodeField;
    private JTextField addEquipmentNameField;
    private JTextField addEquipmentUnitPriceField;
    private JTextField importEquipmentQuantityField;
    private JButton importEquipmentAddEquipmentButton;
    private JButton importEquipmentImportButton;
    private JTextField importEquipmentECodeField;
    private JTextField importEquipmentPriceEachField;
    private JTextArea importEquipmentTotalPrice;
    private JButton logoutButton;
    private JTextArea importEquipmentLog;
    private JPanel parentPanel;
    private JTextArea importEquipmentResult;
    private JButton importEquipmentClearButton;
    private JTextField addInvoiceMemberNoField;
    private JTextPane addInvoiceTable;
    private JButton addInvoiceAddDetailButton;
    private JTextArea addInvoiceLog;
    private JButton addInvoiceClearButton;
    private JButton addInvoiceAddButton;
    private JTextField addInvoiceEquipmentCodeField;
    private JTextField addInvoiceQuantityField;
    private JTextArea addInvoiceTotalPriceText;
    private JTextField searchEquipmentECodeField;
    private JList searchEquipmentNameList;
    private JButton searchEquipmentECodeButton;
    private JTextPane searchEquipmentInformationLog;
    private JTextField searchEquipmentENameField;
    private JButton searchEquipmentENameButton;
    private JTextArea searchEquipmentLog;
    private JPanel addStaffPanel;
    private JButton addMemberClearButton;
    private JButton addStaffClearButton;
    private JButton addEquipmentClearButton;
    private JButton searchEquipmentClearButton;
    private Login log;

    private final String[] header = {"Equipment Code", "Import Quantity", "Price Each"};
    private Vector<String[]> currentList = new Vector<>();
    private String currentString = "";
    private float totalPrice = 0;

    private float addInvoiceTotalPrice = 0;
    private String addInvoiceCurrentList = "";
    private Vector<String[]> addInvoiceList = new Vector<>();
    private DefaultListModel<String> listEquipment = new DefaultListModel<>();
    private Vector<equipment> rs = null;

    private int currentId;

    public void setActive(boolean active) {
        this.setVisible(active);
        this.setEnabled(active);

        if (active) {
            staffNameField.setText("Hello, " + accountManagement.getJobtitle() + " " + accountManagement.getName() + ", Staff number: " + accountManagement.getStaffNo());
        }
    }

    public void setViewManager(boolean isManager) {
        addStaffPanel.setVisible(isManager);
    }

    public void setLog(Login log) {
        this.log = log;
    }

    public mainMenu() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);

        addEquipmentUnitPriceField.setText("0");
        addMemberAddButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String fname = addMemberFirstNameField.getText();
                String sname = addMemberLastNameField.getText();
                String phoneNum = addMemberPhoneNumField.getText();

                try {
                    memberQuery.createMember(fname, sname, phoneNum);

                    addMemberFirstNameField.setText("");
                    addMemberLastNameField.setText("");
                    addMemberPhoneNumField.setText("");

                    addMemberLogField.setText(
                            "Add new member" + '\n'
                            + "Name: " + fname + " " + sname + "\n"
                            + "phone number: " + phoneNum
                    );
                } catch (memberQueryException exc) {
                    exc.printStackTrace();
                }
            }
        });
        addStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addStaffButton) {
                    if (!accountManagement.getJobtitle().equals("Manager")) {
                        addStaffLog.setText("Only Manager can add new staff account.");
                        return;
                    }
                    String fname = addStaffFirstNameField.getText();
                    String lname = addStaffLastNameField.getText();
                    String phoneNum = addStaffPhoneNumField.getText();
                    String jobtitle = addStaffJobTitleField.getText();
                    String username = addStaffUernameField.getText();
                    String password = String.valueOf(addStaffPasswordField.getPassword());
                    String vpassword = String.valueOf(addStaffVerifyPasswordField.getPassword());

                    if (!password.equals(vpassword)) {
                        addStaffLog.setText("Password and verify password does not match.");
                    } else {
                        try {
                            PreparedStatement ps = mysql_connection.getConn().prepareStatement("call login(?,?);");
                            ps.setString(1, username);
                            ps.setString(2, password);

                            ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                throw new staffQueryException("Duplicated account.");
                            }
                            staffQuery.createStaff(fname, lname, phoneNum, jobtitle, username, password);

                            addStaffLog.setText("Create staff complete.");
                        } catch (staffQueryException | SQLException exc) {
                            exc.printStackTrace();
                            addStaffLog.setText(exc.getMessage());
                        }
                    }
                }
            }
        });

        addEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addEquipmentButton) {
                    String eCode = addEquipmentCodeField.getText();
                    String eName = addEquipmentNameField.getText();
                    boolean eIsSellable = addEquipmentSellableButton.isSelected();
                    float unitPrice = Float.parseFloat(addEquipmentUnitPriceField.getText());

                    try {
                        equipmentQuery.insertEquipment(eCode, eName, eIsSellable, unitPrice);

                        if (!eIsSellable) {
                            unitPrice = 0;
                        }

                        addEquipmentLog.setText("New Equipment Created.");
                    } catch (equipmentQueryException exc) {
                        exc.printStackTrace();
                        addEquipmentLog.setText(exc.getMessage());
                    }
                }
            }
        });
        addEquipmentSellableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addEquipmentSellableButton) {
                    if (addEquipmentSellableButton.isSelected()) {
                        addEquipmentUnitPriceField.setEnabled(true);
                    } else {
                        addEquipmentUnitPriceField.setEnabled(false);
                    }
                }
            }
        });
        importEquipmentAddEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importEquipmentAddEquipmentButton) {
                    importEquipmentLog.setText("");
                    String eCode = importEquipmentECodeField.getText();
                    String importQuantity = importEquipmentQuantityField.getText();
                    String priceEach = importEquipmentPriceEachField.getText();

                    if (eCode.equals("") || importQuantity.equals("") || priceEach.equals("")) {
                        importEquipmentLog.setText("Fields must be filled.");
                    } else {
                        currentList.add(new String[]{eCode, importQuantity, priceEach});
                        totalPrice += Float.parseFloat(priceEach) * Integer.parseInt(importQuantity);
                        importEquipmentTotalPrice.setText(String.valueOf(totalPrice));

                        currentString = currentString + "\n"
                                + "Equipment Code: " + eCode + " importQuantity: " + importQuantity + " Price each: " + priceEach;
                        importEquipmentResult.setText(currentString);
                    }
                }
            }
        });
        importEquipmentImportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importEquipmentImportButton) {
                    if (currentList.isEmpty()) {
                        importEquipmentLog.setText("No equipment import.");
                    } else {
                        try {
                            importEquipmentQuery.insertImport(accountManagement.getStaffNo());

                            for (int i = 0; i <currentList.size(); i++) {
                                importEquipmentQuery.insertImportDetail(
                                        currentList.get(i)[0],
                                        Integer.parseInt(currentList.get(i)[1]),
                                        Float.parseFloat(currentList.get(i)[2])
                                );
                            }

                            importEquipmentLog.setText("Import complete.");
                        } catch (importEquipmentException exc) {
                            exc.printStackTrace();
                            importEquipmentLog.setText(exc.getMessage());
                        }
                    }
                }
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == logoutButton) {
                    clearAll();
                    try {
                        accountManagement.logout();
                    } catch (accountException accountException) {
                        accountException.printStackTrace();
                    }
                    setActive(false);
                    log.setActive(true);
                }
            }
        });
        addInvoiceAddDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addInvoiceAddDetailButton) {
                    String eCode = addInvoiceEquipmentCodeField.getText();
                    String quantity = addInvoiceQuantityField.getText();

                    if (eCode.equals("") || quantity.equals("")) {
                        addInvoiceLog.setText("Fields must be filled.");
                    } else {
                        ResultSet rs = null;
                        try {
                            rs = equipmentQuery.getEquipment(eCode);
                        } catch (equipmentQueryException equipmentQueryException) {
                            equipmentQueryException.printStackTrace();
                            addInvoiceLog.setText(equipmentQueryException.getMessage());
                            return;
                        }
                        try {
                            if (!rs.getBoolean(3)) {
                                addInvoiceLog.setText("Equipment not for sale.");
                                return;
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            addInvoiceLog.setText(throwables.getMessage());
                            return;
                        }
                        for (int i = 0; i < addInvoiceList.size(); i++) {
                            if (eCode.equals(addInvoiceList.get(i)[0])) {
                                addInvoiceLog.setText("Duplicated equipment code.");
                                return;
                            }
                        }
                        try {
                            if (Integer.parseInt(quantity) < 1) {
                                addInvoiceLog.setText("Number of equipment should be positive.");
                                return;
                            }
                            if (Integer.parseInt(quantity) > equipmentQuery.getRemain(eCode)) {
                                addInvoiceLog.setText("Equipment out of stock.");
                                return;
                            }
                        } catch (equipmentQueryException equipmentQueryException) {
                            equipmentQueryException.printStackTrace();
                        }
                        float unitPrice = 0;
                        try {
                            unitPrice = equipmentQuery.getEquipmentPrice(eCode);
                            addInvoiceTotalPrice += Integer.parseInt(quantity) * unitPrice;
                            addInvoiceCurrentList += '\n' + "Equipment Code: " + eCode + " Quantity: " + quantity + " Price Each: " + unitPrice;
                            addInvoiceTable.setText(addInvoiceCurrentList);
                            addInvoiceTotalPriceText.setText(String.valueOf(addInvoiceTotalPrice));
                            addInvoiceList.add(new String[]{eCode, quantity});
                            addInvoiceLog.setText("Add into cart complete.");
                        } catch (equipmentQueryException equipmentQueryException) {
                            equipmentQueryException.printStackTrace();
                        }
                    }
                }
            }
        });
        addInvoiceAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() != addInvoiceAddButton) return;
                try {
                    invoiceQuery.insertInvoice(accountManagement.getStaffNo(), Integer.parseInt(addInvoiceMemberNoField.getText()));

                    for (int i = 0; i < addInvoiceList.size(); i++) {
                        invoiceQuery.insertInvoiceDetail(
                                addInvoiceList.get(i)[0],
                                Integer.parseInt(addInvoiceList.get(i)[1])
                        );
                    }

                    addInvoiceMemberNoField.setText("");
                    addInvoiceEquipmentCodeField.setText("");
                    addInvoiceQuantityField.setText("");
                    addInvoiceLog.setText("Create Invoice complete.");
                    addInvoiceTable.setText("");
                    addInvoiceTotalPriceText.setText("");
                    addInvoiceTotalPrice = 0;
                    addInvoiceCurrentList = "";
                    addInvoiceList.clear();
                } catch (invoiceQueryException invoiceQueryException) {
                    invoiceQueryException.printStackTrace();
                }
            }
        });
        searchEquipmentENameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchEquipmentENameButton) {
                    try {
                        searchEquipmentLog.setText("");
                        String eName = searchEquipmentENameField.getText();
                        rs = equipmentQuery.findEquipmentByName(eName);

                        DefaultListModel<String> currentList = new DefaultListModel<>();
                        for (int i = 0; i < rs.size(); i++) {
                            currentList.add(currentList.size(), rs.get(i).geteName());
                        }

                        listEquipment = currentList;
                        searchEquipmentNameList.setModel(listEquipment);

                    } catch (equipmentQueryException exc) {
                        searchEquipmentLog.setText(exc.getMessage());
                    }
                }
            }
        });
        searchEquipmentNameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (searchEquipmentNameList.isSelectionEmpty()) {
                        return;
                    }
                    searchEquipmentLog.setText("");
                    int id = searchEquipmentNameList.getSelectedIndex();
                    currentId = id;
                    String detail = "Equipment Code: " + rs.get(currentId).geteCode() + "\n"
                            + "Equipment Name: " + rs.get(currentId).geteName() + "\n"
                            + "Sellable: " + rs.get(currentId).iseIsSellable() + "\n"
                            + "Unit Price: " + rs.get(currentId).getUnitPrice() + "\n"
                            + "Remain stock: " + equipmentQuery.getRemain(rs.get(currentId).geteCode());

                    searchEquipmentInformationLog.setText(detail);
                    searchEquipmentECodeField.setText(rs.get(currentId).geteCode());
                } catch (equipmentQueryException exc) {
                    exc.printStackTrace();
                    searchEquipmentLog.setText(exc.getMessage());
                }
            }
        });
        searchEquipmentECodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchEquipmentLog.setText("");
                    String eCode = searchEquipmentECodeField.getText();

                    ResultSet rss = equipmentQuery.getEquipment(eCode);

                    String detail = "Equipment Code: " + rss.getString(1) + "\n"
                            + "Equipment Name: " + rss.getString(2) + "\n"
                            + "Sellable: " + rss.getBoolean(3) + "\n"
                            + "Unit Price: " + rss.getFloat(4) + "\n"
                            + "Remain stock: " + equipmentQuery.getRemain(rss.getString(1));
                    searchEquipmentInformationLog.setText(detail);
                } catch (equipmentQueryException exc) {
                    exc.printStackTrace();
                    searchEquipmentLog.setText(exc.getMessage());
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    searchEquipmentLog.setText("SQL Issue. Cannot find.");
                }
            }
        });
        addMemberClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addMemberClearButton) {
                    addMemberClear();
                }
            }
        });
        addStaffClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addStaffClearButton) {
                    addStaffClear();
                }
            }
        });
        addEquipmentClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addEquipmentClearButton) {
                    addEquipmentClear();
                }
            }
        });
        searchEquipmentClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchEquipmentClearButton) {
                    searchEquipmentClear();
                }
            }
        });
        addInvoiceClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() != addInvoiceClearButton) return;
                addInvoiceClear();
            }
        });
        importEquipmentClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importEquipmentClearButton) {
                    importEquipmentClear();
                }
            }
        });
        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearAll();
            }
        });
    }

    private void clearAll() {
        addMemberClear();
        addStaffClear();
        addEquipmentClear();
        searchEquipmentClear();
        addInvoiceClear();
        importEquipmentClear();
    }

    private void addMemberClear() {
        addMemberFirstNameField.setText("");
        addMemberLastNameField.setText("");
        addMemberPhoneNumField.setText("");
        addMemberLogField.setText("");
    }

    private void addStaffClear() {
        addStaffFirstNameField.setText("");
        addStaffLastNameField.setText("");
        addStaffPhoneNumField.setText("");
        addStaffJobTitleField.setText("");
        addStaffUernameField.setText("");
        addStaffPasswordField.setText("");
        addStaffVerifyPasswordField.setText("");
        addStaffLog.setText("");
    }

    private void addEquipmentClear() {
        addEquipmentCodeField.setText("");
        addEquipmentNameField.setText("");
        addEquipmentSellableButton.setSelected(true);
        addEquipmentUnitPriceField.setText("0.0");
        addEquipmentLog.setText("");
    }

    private void searchEquipmentClear() {
        searchEquipmentECodeField.setText("");
        searchEquipmentECodeField.setText("");
        searchEquipmentInformationLog.setText("");
        searchEquipmentENameField.setText("");
        listEquipment.clear();
        searchEquipmentNameList.setModel(listEquipment);
    }

    private void addInvoiceClear() {
        addInvoiceMemberNoField.setText("");
        addInvoiceEquipmentCodeField.setText("");
        addInvoiceQuantityField.setText("");
        addInvoiceLog.setText("");
        addInvoiceTable.setText("");
        addInvoiceTotalPriceText.setText("");
        addInvoiceTotalPrice = 0;
        addInvoiceCurrentList = "";
        addInvoiceList.clear();
    }

    private void importEquipmentClear() {
        importEquipmentECodeField.setText("");
        importEquipmentQuantityField.setText("");
        importEquipmentPriceEachField.setText("");
        importEquipmentLog.setText("");
        currentString = "";
        currentList.clear();
        importEquipmentResult.setText("");
        totalPrice = 0;
    }

    public static void main(String[] args) {
        mainMenu menu = new mainMenu();
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menu.pack();
        menu.setVisible(true);
    }
}
