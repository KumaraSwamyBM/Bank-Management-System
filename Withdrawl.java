package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.sql.*;

public class Withdrawl extends JFrame implements ActionListener {

    JTextField t1;
    JButton b1, b2;
    JLabel l1, l2;
    String pin;

    Withdrawl(String pin) {
        this.pin = pin;

        // Background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 960, 1080);
        add(l3);

        l1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setBounds(190, 350, 400, 20);
        l3.add(l1);

        l2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));
        l2.setBounds(190, 400, 400, 20);
        l3.add(l2);

        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));
        t1.setBounds(190, 450, 330, 30);
        l3.add(t1);

        b1 = new JButton("WITHDRAW");
        b2 = new JButton("BACK");
        b1.setBounds(390, 588, 150, 35);
        b2.setBounds(390, 633, 150, 35);
        l3.add(b1);
        l3.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            double amount = 0;
            if (ae.getSource() == b1) {
                if (t1.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Withdraw");
                    return;
                }

                amount = Double.parseDouble(t1.getText()); // <-- use double

                Conn c1 = new Conn();
                ResultSet rs = c1.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
                double balance = 0;

                while (rs.next()) {
                    String mode = rs.getString("mode");
                    double amt = Double.parseDouble(rs.getString("amount"));
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

                // Insert withdrawal as DECIMAL
                Date date = new Date();
                c1.s.executeUpdate("INSERT INTO bank(pin, date, mode, amount) VALUES('" + pin + "', '" + new java.sql.Timestamp(date.getTime()) + "', 'Withdrawl', " + amount + ")");
                JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");

                setVisible(false);
                new Transactions(pin).setVisible(true);

            } else if (ae.getSource() == b2) {
                setVisible(false);
                new Transactions(pin).setVisible(true);
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric amount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Withdrawl("").setVisible(true);
    }
}
