package Backend.Servlets.RequestBodyObjects;

public class NewEventBody {
    private String name;
    private String location;
    private boolean attending;
    private int capacity;
    private int organizer;
    private String date;
    private int price;
    private int id;

    public NewEventBody(String name, String location, boolean attending, int capacity, int organizer, String date, int price) {
        this.name = name;
        this.location = location;
        this.attending = attending;
        this.capacity = capacity;
        this.organizer = organizer;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean getAttending() {
        return attending;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOrganizer() {
        return organizer;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public void setOrganizer(int organizer) {
        this.organizer = organizer;
    }

    @Override
    public String toString() {
        return "NewEvent{" +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", attending=" + attending +
                ", capacity=" + capacity +
                ", organizer='" + organizer + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
