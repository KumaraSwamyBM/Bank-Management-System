package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Pin extends JFrame implements ActionListener {

    JPasswordField t1, t2;
    JButton b1, b2;
    JLabel l1, l2, l3;
    String pin;

    public Pin(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l4 = new JLabel(i3);
        l4.setBounds(0, 0, 960, 1080);
        add(l4);

        l1 = new JLabel("CHANGE YOUR PIN");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setBounds(250, 300, 500, 35);
        l4.add(l1);

        l2 = new JLabel("New PIN:");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));
        l2.setBounds(180, 350, 200, 35);
        l4.add(l2);

        l3 = new JLabel("Re-Enter New PIN:");
        l3.setForeground(Color.WHITE);
        l3.setFont(new Font("System", Font.BOLD, 16));
        l3.setBounds(180, 400, 200, 35);
        l4.add(l3);

        t1 = new JPasswordField();
        t1.setBounds(350, 350, 180, 30);
        t1.setFont(new Font("Raleway", Font.BOLD, 18));
        l4.add(t1);

        t2 = new JPasswordField();
        t2.setBounds(350, 400, 180, 30);
        t2.setFont(new Font("Raleway", Font.BOLD, 18));
        l4.add(t2);

        b1 = new JButton("CHANGE");
        b1.setBounds(390, 588, 150, 35);
        l4.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(390, 633, 150, 35);
        l4.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {

            String newPin = String.valueOf(t1.getPassword());
            String rePin = String.valueOf(t2.getPassword());

            if (!newPin.equals(rePin)) {
                JOptionPane.showMessageDialog(null, "PIN Does Not Match!");
                return;
            }

            if (newPin.equals("")) {
                JOptionPane.showMessageDialog(null, "PIN cannot be empty");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                con.setAutoCommit(false);  // Start transaction

                // Update login table
                PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE login SET pin=? WHERE pin=?"
                );
                ps1.setString(1, newPin);
                ps1.setString(2, pin);
                ps1.executeUpdate();

                // Update bank table
                PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE bank SET pin=? WHERE pin=?"
                );
                ps2.setString(1, newPin);
                ps2.setString(2, pin);
                ps2.executeUpdate();

                // Update upi_accounts table
                PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE upi_accounts SET pin=? WHERE pin=?"
                );
                ps3.setString(1, newPin);
                ps3.setString(2, pin);
                ps3.executeUpdate();

                con.commit();  // All updates succeeded

                JOptionPane.showMessageDialog(null, "PIN Changed Successfully!");

                setVisible(false);
                new Transactions(newPin).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Changing PIN");
            }

        } else if (ae.getSource() == b2) {

            setVisible(false);
            new Transactions(pin).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Pin("").setVisible(true);
    }
}
