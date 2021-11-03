package client.graphics;

import client.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RoomWindow extends JFrame implements Runnable {
    private final JTextField inputMsg;

    DefaultTableModel messagesModel;
    JTable messages;
    JScrollPane messagesPane;

    JTable users;
    DefaultTableModel usersModel;
    JScrollPane usersPane;


    public RoomWindow(int port, String clientName) {
        Client c = new Client(port, this, clientName);
        inputMsg = new JTextField();

        usersModel = new DefaultTableModel(0, 1);
        users = new JTable(usersModel);
        users.setTableHeader(null);
        usersPane = new JScrollPane(users);

        messagesModel = new DefaultTableModel(0, 1);
        messages = new JTable(messagesModel);
        messages.setTableHeader(null);
        messagesPane = new JScrollPane(messages);


        messages.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                messages.scrollRectToVisible(messages.getCellRect(messages.getRowCount() - 1, 0, true));
            }
        });

        JButton refreshUsers = new JButton("Refresh users");

        refreshUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                c.updateUserList();
            }
        });

        pack();
//        setSize(new Dimension(600, 600));
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setFocusable(true);
        setLayout(new BorderLayout());

        usersPane.setSize(new Dimension(50, 600));
        JPanel panelLeft = new JPanel();
        panelLeft.setSize(new Dimension(300, 600));
        panelLeft.setLayout(new BorderLayout());
        panelLeft.setSize(new Dimension(300, 600));
        panelLeft.add(inputMsg, BorderLayout.PAGE_END);
        panelLeft.add(messagesPane, BorderLayout.CENTER);
        panelLeft.add(refreshUsers, BorderLayout.PAGE_START);
        add(panelLeft, BorderLayout.WEST);
        add(usersPane, BorderLayout.EAST);

        inputMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    c.send(inputMsg.getText());
                    inputMsg.setText("");
                }
            }
        });
    }

    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shorMsg(String msg) {
        messagesModel.addRow(new Object[]{
                msg
        });
    }

    public void updateUserTable(ArrayList<String> list) {
        usersModel.getDataVector().removeAllElements();
        for (String name : list) {
            usersModel.addRow(new Object[]{name});
        }
    }
}
