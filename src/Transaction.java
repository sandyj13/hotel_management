import java.util.Date;

public class Transaction {
    private int transactionId;
    private int guestId;
    private double amount;
    private String date;
    private String type;

    // Constructor
    public Transaction(int guestId, double amount, String date, String type) {
        this.guestId = guestId;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public Transaction(int transactionId, int guestId, double amount, String date, String type) {
        this.transactionId = transactionId;
        this.guestId = guestId;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", guestId=" + guestId +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
