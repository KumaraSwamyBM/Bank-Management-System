package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class BalanceEnquiry extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1;
    String pin;

    BalanceEnquiry(String pin) {
        this.pin = pin;

        // Background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 960, 1080);
        add(l3);

        l1 = new JLabel();
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setBounds(190, 350, 600, 35);
        l3.add(l1);

        b1 = new JButton("BACK");
        b1.setBounds(390, 633, 150, 35);
        l3.add(b1);
        b1.addActionListener(this);

        double balance = 0;
        try {
            Conn c1 = new Conn();
            ResultSet rs = c1.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            while (rs.next()) {
                String mode = rs.getString("mode");
                double amt = Double.parseDouble(rs.getString("amount"));  // <-- Use double
                if (mode.equals("Deposit")) {
                    balance += amt;
                } else {
                    balance -= amt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        l1.setText("Your Current Account Balance is Rs " + balance);

        setSize(960, 1080);
        setUndecorated(true);
        setLocation(500, 0);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Transactions(pin).setVisible(true);
    }

    public static void main(String[] args) {
        new BalanceEnquiry("").setVisible(true);
    }
}
