public class Feedback {
    private int feedbackId;
    private int guestId;
    private String feedbackText;
    private int rating;
    private java.sql.Date feedbackDate;

    public Feedback(int feedbackId, int guestId, String feedbackText, int rating, java.sql.Date feedbackDate) {
        this.feedbackId = feedbackId;
        this.guestId = guestId;
        this.feedbackText = feedbackText;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getGuestId() {
        return guestId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public int getRating() {
        return rating;
    }

    public java.sql.Date getFeedbackDate() {
        return feedbackDate;
    }
}
