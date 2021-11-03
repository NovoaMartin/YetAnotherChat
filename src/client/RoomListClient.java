package client;

import dto.RoomDTO;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class RoomListClient extends Thread implements Runnable {
    Socket socket;
    private final ObjectInputStream objectInputStream;
    private final DataOutputStream outputStream;

    private boolean isRunning = true;

    ArrayList<RoomDTO> lista;

    public RoomListClient() throws IOException {
        this.socket = new Socket("localhost", 3333);
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public ArrayList<RoomDTO> getLista() {
        return lista;
    }


    public void refreshList() {
        try {
            outputStream.writeUTF("LIST");
            //noinspection unchecked
            lista = (ArrayList<RoomDTO>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (isRunning) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println("ERROR connecting to roomManager server");
        }


    }

    public void send(String text) {
        try{
            outputStream.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

