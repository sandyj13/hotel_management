public class RoomService {
    private int serviceId;
    private String serviceName;
    private double price;
    private int roomNumber;

    public RoomService(int serviceId, String serviceName, double price, int roomNumber) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
        this.roomNumber = roomNumber;
    }

    // Getters and setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return "RoomService{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", roomNumber=" + roomNumber +
                '}';
    }
}
