package client.graphics;

import client.RoomListClient;
import dto.RoomDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class RoomListWindow extends JFrame implements Runnable {
    private RoomListClient roomListClient;
    JTable table;
    JScrollPane scrollPane;
    JTextField selectRoom;
    JTextField selectName;
    JButton refreshButton;
    private boolean isRunning = true;

    ArrayList<RoomDTO> roomsList;

    DefaultTableModel model;

    public RoomListWindow() {
        try {
            roomListClient = new RoomListClient();

            model = new DefaultTableModel();
            model.setColumnCount(3);
            model.setColumnIdentifiers(new Object[]{"Name", "Port", "Users"});
            table = new JTable(model);
            scrollPane = new JScrollPane(table);
            refreshList();
            refreshTable();

            selectName = new JTextField("Escriba su nombre");
            selectRoom = new JTextField("Escriba el nombre de la sala");
            refreshButton = new JButton("Refresh");
            refreshButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    refreshList();
                    refreshTable();
                }
            });

            selectRoom.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (selectRoom.getText().startsWith("/")) {
                            if (selectRoom.getText().startsWith("/add")) {
                                send(selectRoom.getText());
                                refreshList();
                                refreshTable();
                            }
                        } else
                            joinRoom(selectRoom.getText());
                        selectRoom.setText("");
                    }
                }
            });

            setSize(new Dimension(300, 600));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            setLayout(new BorderLayout());


            add(scrollPane, BorderLayout.CENTER);
            add(refreshButton, BorderLayout.NORTH);
            add(selectRoom, BorderLayout.SOUTH);


        } catch (IOException e) {
            System.out.println("ERROR at connecting to RoomListServer");
            System.exit(-1);
        }
    }

    private void send(String text) {
        roomListClient.send(text);
    }

    private void joinRoom(String name) {
        for (RoomDTO room : roomsList) {
            if (room.name.equals(name)) {
                new Thread(new RoomWindow(room.port, selectName.getText())).start();
                break;
            }
        }
    }

    private void refreshTable() {
        model.getDataVector().removeAllElements();
        for (RoomDTO room : roomsList) {
            model.addRow(new Object[]{room.name, room.port, room.userAmount});
        }
    }

    private void refreshList() {
        System.out.println("Refreshed!");
        roomListClient.refreshList();
        roomsList = roomListClient.getLista();
    }


    @Override
    public void run() {
        while (isRunning) {
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
