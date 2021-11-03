package client;

import client.graphics.RoomWindow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientThread extends Thread implements Runnable {
    private final Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private String inputBuffer = "";
    private String clientName;

    private ArrayList<String> userList;
    RoomWindow window;


    public ClientThread(Socket socket, RoomWindow window, String clientName) {
        this.socket = socket;
        this.window = window;
        this.clientName = clientName;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        send("/name " + clientName);
    }

    public void run() {
        boolean isRunning = true;
        try {
            while (isRunning) {
                if (inputStream.available() != 0) {
                    showMessage(inputStream.readUTF());
                }
                handleMessageOutput();

                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessageOutput() throws IOException {
        if (inputBuffer.length() > 0) {
            if (inputBuffer.startsWith("/")) {
                handleCommand();
            } else {
                sendMessage();
            }
            inputBuffer = "";
        }
    }

    private void sendMessage() throws IOException {
        outputStream.writeUTF(this.clientName + ": " + inputBuffer);
    }

    private void handleCommand() throws IOException {
        if (inputBuffer.startsWith("/name")) {
            clientName = inputBuffer.substring(6);
            outputStream.writeUTF(inputBuffer);
        } else if (inputBuffer.startsWith("/list")) {

            updateUserList();
        }
    }

    public void updateUserList() {
        try {
            outputStream.writeUTF("/list");
            String userString = inputStream.readUTF();
            ArrayList<String> list = new ArrayList<>(Arrays.asList(userString.split(";;;")));
            window.updateUserTable(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void showMessage(String msg) {
        window.shorMsg(msg);
    }

    public void send(String msg) {
        inputBuffer = msg;
    }
}
