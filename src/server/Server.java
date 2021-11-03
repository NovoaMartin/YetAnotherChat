package server;

import dto.RoomDTO;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Thread implements Runnable {
    ServerSocket ss;
    ArrayList<Room> rooms;
    private boolean isRunning = true;


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.addRoom("room1");
        server.start();
    }

    public Server() throws IOException {
        rooms = new ArrayList<>();
        ss = new ServerSocket(3333);
    }

    public void addRoom(String name) {
        Room room = new Room(name);
        room.start();
        rooms.add(room);
    }

    public ArrayList<RoomDTO> getRooms() {
        ArrayList<RoomDTO> roomList = new ArrayList<>();
        for (Room room : rooms) {
            roomList.add(new RoomDTO(room.getRoomName(), room.getPort(), room.getUserAmount()));
        }
        return roomList;
    }


    public void run() {
        while (isRunning) {
            try {
                RoomList roomList = new RoomList(ss.accept(), this);
                roomList.start();
                sleep(50);
            } catch (Exception e) {
                System.out.println("ERROR at accepting socket");
            }

        }
    }
}
