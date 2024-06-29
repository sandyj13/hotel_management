import java.sql.Date;
import java.sql.Time;

public class Amenity {
    private int amenityId;
    private String amenityName;
    private Time timeslot;
    private Date date;
    private int reservationId;

    public Amenity(int amenityId, String amenityName, Time timeslot, Date date, int reservationId) {
        this.amenityId = amenityId;
        this.amenityName = amenityName;
        this.timeslot = timeslot;
        this.date = date;
        this.reservationId = reservationId;
    }

    // Getters and setters

    public int getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(int amenityId) {
        this.amenityId = amenityId;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public Time getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Time timeslot) {
        this.timeslot = timeslot;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
}
