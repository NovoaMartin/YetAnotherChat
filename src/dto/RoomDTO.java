package dto;

import java.io.Serializable;

public class RoomDTO implements Serializable {
    public String name;
    public int port;
    public int userAmount;

    public RoomDTO(String name, int port, int userAmount) {
        this.name = name;
        this.port = port;
        this.userAmount = userAmount;
    }
}