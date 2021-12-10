package Backend.Servlets.RequestBodyObjects;

public class TransferTicketBody {
    private int from;
    private int eventId;
    private String to;

    public TransferTicketBody(int from, int eventId, String email) {
        this.from = from;
        this.eventId = eventId;
        this.to = email;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEmail() {
        return to;
    }

    @Override
    public String toString() {
        return "TransferTicketBody{" +
                "from=" + from +
                ", eventid=" + eventId +
                ", email='" + to + '\'' +
                '}';
    }
}
