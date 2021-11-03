package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class Room extends Thread implements Runnable {
    private ArrayList<User> users;
    private ServerSocket ss;
    private String roomName;
    private int port;

    private boolean isRunning = true;

    public static void main(String[] args) {
        Room room = new Room("room1");
        room.start();
    }

    public Room(String name) {
        try {
            roomName = name;
            users = new ArrayList<>();
            ss = new ServerSocket(0);
            port = ss.getLocalPort();
        } catch (Exception e) {
            System.out.println("ERROR AL INICIAR EL SERVIDOR");
        }
    }

    public void run() {
        try {
            while (isRunning) {
                User user = new User(ss.accept(), this);
                users.add(user);
                user.start();
                sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String msg) {
        Iterator<User> userIterator = users.iterator();
        System.out.println(msg);
        while (userIterator.hasNext()) {
            User user = null;
            try {
                user = userIterator.next();
                user.send(msg);
            } catch (IOException e) {
                user.close();
                userIterator.remove();
            }
        }
    }

    public int getUserAmount() {
        return users.size();
    }

    public ArrayList<String> getUserList() {
        ArrayList<String> list = new ArrayList<>();
        for (User user : users) {
            list.add(user.getUserName());
        }
        return list;
    }

    public void close() {
        isRunning = false;
    }

    public int getPort() {
        return port;
    }

    public String getRoomName() {
        return roomName;
    }
}
