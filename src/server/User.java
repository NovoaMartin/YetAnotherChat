package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User extends Thread implements Runnable {
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Room room;

    private String userName;

    boolean isRunning = true;

    public User(Socket socket, Room room) {
        try {
            this.socket = socket;
            this.room = room;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (isRunning) {
            try {
                String msg = receive();
                if (msg != null) {
                    if (msg.length() > 0) {
                        if (msg.startsWith("/")) {
                            handleCommand(msg);
                        } else {
                            room.broadcast(msg);
                        }
                    }
                }
                Thread.sleep(50);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleCommand(String msg) throws IOException {
        if (msg.startsWith("/name")) {
            this.userName = msg.substring(6);
        } else if (msg.startsWith("/list")) {
            StringBuilder list = new StringBuilder();
            for (String name : room.getUserList()) {
                list.append(name).append(";;;");
            }
            outputStream.writeUTF(list.toString());
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

    public void send(String msg) throws IOException {
        outputStream.writeUTF(msg);
    }

    public void close() {
        isRunning = false;
    }

    public String getUserName() {
        return userName;
    }
}
