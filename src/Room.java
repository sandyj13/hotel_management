public class Room {
    private int roomNumber;
    private String roomType;
    private int capacity;
    private int guestId;

    public Room(int roomNumber, String roomType, int capacity, int guestId) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.guestId = guestId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getGuestId() {
        return guestId;
    }
}
