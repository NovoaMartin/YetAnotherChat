package server;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RoomList extends Thread implements Runnable {
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    ObjectOutputStream objectOutputStream;
    Server roomManager;

    boolean isRunning = true;

    public RoomList(Socket socket, Server roomManager) {
        try {
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream((socket.getOutputStream()));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.roomManager = roomManager;
        } catch (IOException e) {
            System.out.println("ERROR on RoomList connection");
        }
    }

    public void run() {
        try {
            while (isRunning) {
                String s = receive();
                if (Objects.equals(s, "LIST")) {
                    objectOutputStream.writeObject(roomManager.getRooms());
                } else if (s.startsWith("/add")) {
                    roomManager.addRoom(s.substring(5));
                }
                sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        String s;
        try {
            s = inputStream.readUTF();
            if (s.length() == 0) {
                return null;
            }
        } catch (IOException e) {
            return "";
        }
        return s;
    }

}
