package Backend.Servlets.RequestBodyObjects;

public class User {
    /**
     * Naming convention is the way it is because database stores the name in that format
     */
    private String First_Name;
    private String Last_Name;
    private String Email;
    private String Preferred_Name;
    private int id;

    public User(String firstName, String lastName, String email, String preferredName, int id) {
        this.First_Name = firstName;
        this.Last_Name = lastName;
        this.Email = email;
        this.id = id;
        this.Preferred_Name = preferredName;
    }

    public String getPreferredName() {
        return Preferred_Name;
    }

    public String getFirstName() {
        return First_Name;
    }

    public String getLastName() {
        return Last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + First_Name + '\'' +
                ", lastName='" + Last_Name + '\'' +
                ", email='" + Email + '\'' +
                ", preferredName='" + Preferred_Name + '\'' +
                ", id=" + id +
                '}';
    }

}
