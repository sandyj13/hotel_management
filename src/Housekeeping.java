import java.util.Date;

public class Housekeeping {
    private int housekeepingId;
    private int staffId;
    private int roomNumber;
    private String task;
    private boolean completionStatus;
    private Date date;

    public Housekeeping(int housekeepingId, int staffId, int roomNumber, String task, boolean completionStatus, Date date) {
        this.housekeepingId = housekeepingId;
        this.staffId = staffId;
        this.roomNumber = roomNumber;
        this.task = task;
        this.completionStatus = completionStatus;
        this.date = date;
    }

    public int getHousekeepingId() {
        return housekeepingId;
    }

    public void setHousekeepingId(int housekeepingId) {
        this.housekeepingId = housekeepingId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
