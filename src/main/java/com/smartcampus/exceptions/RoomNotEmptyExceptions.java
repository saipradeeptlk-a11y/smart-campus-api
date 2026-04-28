package com.smartcampus.exceptions;

public class RoomNotEmptyExceptions extends RuntimeException {
    private final String roomId;

    public RoomNotEmptyExceptions(String roomId) {
        super("Room " + roomId + " still has sensors assigned to it.");
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
