package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    FastCash(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 960, 1080);
        add(l3);

        l1 = new JLabel("SELECT WITHDRAWAL AMOUNT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setBounds(235, 400, 700, 35);
        l3.add(l1);

        b1 = new JButton("Rs 100");
        b2 = new JButton("Rs 500");
        b3 = new JButton("Rs 1000");
        b4 = new JButton("Rs 2000");
        b5 = new JButton("Rs 5000");
        b6 = new JButton("Rs 10000");
        b7 = new JButton("BACK");

        b1.setBounds(170, 499, 150, 35);
        b2.setBounds(390, 499, 150, 35);
        b3.setBounds(170, 543, 150, 35);
        b4.setBounds(390, 543, 150, 35);
        b5.setBounds(170, 588, 150, 35);
        b6.setBounds(390, 588, 150, 35);
        b7.setBounds(390, 633, 150, 35);

        l3.add(b1);
        l3.add(b2);
        l3.add(b3);
        l3.add(b4);
        l3.add(b5);
        l3.add(b6);
        l3.add(b7);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);

        setLayout(null);
        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            // BACK BUTTON HANDLING FIRST
            if (ae.getSource() == b7) {
                setVisible(false);
                new Transactions(pin).setVisible(true);
                return;
            }

            // Extract amount (skip "Rs ")
            String amountStr = ((JButton) ae.getSource()).getText().substring(3);
            double amount = Double.parseDouble(amountStr);

            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            double balance = 0;

            while (rs.next()) {
                String mode = rs.getString("mode");
                double amt = rs.getDouble("amount");
                if (mode.equals("Deposit")) {
                    balance += amt;
                } else {
                    balance -= amt;
                }
            }

            if (balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance");
                return;
            }

            // Insert withdrawal
            Date date = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            c.s.executeUpdate("INSERT INTO bank(pin, date, mode, amount) VALUES('" + pin + "', '" + timestamp + "', 'Withdrawl', " + amount + ")");

            JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");

            setVisible(false);
            new Transactions(pin).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new FastCash("").setVisible(true);
    }
}
