package Backend.Servlets.RequestBodyObjects;

public class TransferTicketBody {
    private int from;
    private int eventid;
    private String email;

    public TransferTicketBody(int from, int eventId, String email) {
        this.from = from;
        this.eventid = eventId;
        this.email = email;
    }

    public int getFrom() {
        return from;
    }

    public int getEventId() {
        return eventid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "TransferTicketBody{" +
                "from=" + from +
                ", eventid=" + eventid +
                ", email='" + email + '\'' +
                '}';
    }
}
