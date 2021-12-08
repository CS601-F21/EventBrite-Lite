package Backend.Servlets.RequestBodyObjects;

public class TransferTicketBody {
    private int from;
    private int eventid;
    private String to;

    public TransferTicketBody(int from, int eventId, String email) {
        this.from = from;
        this.eventid = eventId;
        this.to = email;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getEventId() {
        return eventid;
    }

    public String getEmail() {
        return to;
    }

    @Override
    public String toString() {
        return "TransferTicketBody{" +
                "from=" + from +
                ", eventid=" + eventid +
                ", email='" + to + '\'' +
                '}';
    }
}
