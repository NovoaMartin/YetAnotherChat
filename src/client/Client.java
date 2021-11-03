package client;

import client.graphics.RoomListWindow;
import client.graphics.RoomWindow;

import java.io.IOException;
import java.net.Socket;

public class Client {
    ClientThread thread;

    public static void main(String[] args) {

        RoomListWindow roomListWindow = new RoomListWindow();
        new Thread(roomListWindow).start();
//        RoomListClient roomListClient = new RoomListClient();
//        roomListClient.start();

//        RoomWindow window = new RoomWindow(64215);
//        Thread tw = new Thread(window);
//        tw.start();
    }

    public Client(int port, RoomWindow window, String clientName) {
        try {
            Socket socket = new Socket("localhost", port);
            thread = new ClientThread(socket, window, clientName);
            thread.start();
        } catch (IOException e) {
            System.out.println("ERROR: room server not found");
            System.exit(-1);
        }
    }

    public void send(String msg) {
        thread.send(msg);
    }

    public void updateUserList() {
        thread.updateUserList();
    }
}
