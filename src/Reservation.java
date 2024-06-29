public class Reservation {
    private int reservationId;
    private int guestId;
    private double totalCost;
    private String reservationFor;

    public Reservation(int reservationId, int guestId, double totalCost, String reservationFor) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.totalCost = totalCost;
        this.reservationFor = reservationFor;
    }

    public Reservation(int guestId, double totalCost, String reservationFor) {
        this(0, guestId, totalCost, reservationFor);
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(String reservationFor) {
        this.reservationFor = reservationFor;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", guestId=" + guestId +
                ", totalCost=" + totalCost +
                ", reservationFor='" + reservationFor + '\'' +
                '}';
    }
}
