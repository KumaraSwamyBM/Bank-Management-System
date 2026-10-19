package ASimulatorSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UPIPayment extends JFrame implements ActionListener {

    JTextField receiverUpiField, amountField;
    JButton payButton, backButton;
    JLabel senderUpiLabel;
    String senderPin, senderUpi;

    public UPIPayment(String pin) {
        this.senderPin = pin;
        setLayout(null);

        JLabel title = new JLabel("UPI PAYMENT");
        title.setBounds(200, 20, 400, 40);
        title.setFont(new Font("System", Font.BOLD, 28));
        add(title);

        // ✅ Fetch sender UPI ID using their PIN
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT upi_id FROM upi_accounts WHERE pin = '" + senderPin + "'");
            if (rs.next()) {
                senderUpi = rs.getString("upi_id");
            } else {
                senderUpi = "Not Linked";
            }
        } catch (Exception e) {
            senderUpi = "Error fetching UPI";
            e.printStackTrace();
        }

        JLabel senderLabel = new JLabel("Your UPI ID:");
        senderLabel.setBounds(100, 80, 200, 30);
        senderLabel.setFont(new Font("System", Font.BOLD, 18));
        add(senderLabel);

        senderUpiLabel = new JLabel(senderUpi);
        senderUpiLabel.setBounds(300, 80, 300, 30);
        senderUpiLabel.setFont(new Font("System", Font.PLAIN, 18));
        add(senderUpiLabel);

        JLabel receiverLabel = new JLabel("Receiver UPI ID:");
        receiverLabel.setBounds(100, 130, 200, 30);
        receiverLabel.setFont(new Font("System", Font.BOLD, 18));
        add(receiverLabel);

        receiverUpiField = new JTextField();
        receiverUpiField.setBounds(300, 130, 200, 30);
        add(receiverUpiField);

        JLabel amountLabel = new JLabel("Enter Amount:");
        amountLabel.setBounds(100, 180, 200, 30);
        amountLabel.setFont(new Font("System", Font.BOLD, 18));
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(300, 180, 200, 30);
        add(amountField);

        payButton = new JButton("Pay");
        payButton.setBounds(150, 250, 150, 35);
        add(payButton);

        backButton = new JButton("Back");
        backButton.setBounds(350, 250, 150, 35);
        add(backButton);

        payButton.addActionListener(this);
        backButton.addActionListener(this);

        getContentPane().setBackground(Color.WHITE);
        setSize(700, 400);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == payButton) {
            String receiverUpi = receiverUpiField.getText().trim();
            String amountText = amountField.getText().trim();

            if (receiverUpi.equals("") || amountText.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter all details");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount!");
                return;
            }

            try {
                Conn c = new Conn();

                // ✅ Verify receiver UPI exists
                ResultSet rs2 = c.s.executeQuery("SELECT pin FROM upi_accounts WHERE upi_id='" + receiverUpi + "'");
                if (!rs2.next()) {
                    JOptionPane.showMessageDialog(null, "Invalid Receiver UPI ID!");
                    return;
                }
                String receiverPin = rs2.getString("pin");

                // ✅ Calculate sender balance
                double balance = 0;
                ResultSet rs3 = c.s.executeQuery("SELECT * FROM bank WHERE pin='" + senderPin + "'");
                while (rs3.next()) {
                    if (rs3.getString("mode").equalsIgnoreCase("Deposit"))
                        balance += rs3.getDouble("amount");
                    else
                        balance -= rs3.getDouble("amount");
                }

                if (balance < amount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance!");
                    return;
                }

                // ✅ Deduct from sender (use 'Withdrawl' in mode)
                String q1 = "INSERT INTO bank (pin, date, mode, amount) VALUES ('" + senderPin + "', NOW(), 'Withdrawl', '" + amount + "')";
                c.s.executeUpdate(q1);

                // ✅ Credit to receiver (use 'Deposit' in mode)
                String q2 = "INSERT INTO bank (pin, date, mode, amount) VALUES ('" + receiverPin + "', NOW(), 'Deposit', '" + amount + "')";
                c.s.executeUpdate(q2);

                JOptionPane.showMessageDialog(null, "✅ Payment Successful!\n₹" + amount + " sent to " + receiverUpi);
                setVisible(false);
                new Transactions(senderPin).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Transaction Failed! Check console for details.");
            }
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            new Transactions(senderPin).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new UPIPayment("").setVisible(true);
    }
}
