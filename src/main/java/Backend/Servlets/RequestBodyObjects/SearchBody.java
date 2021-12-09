package Backend.Servlets.RequestBodyObjects;

public class SearchBody {
    private String containsExactWord;
    private int priceLessThan;
    private String location;

    public SearchBody(String containsExactWord, int priceLessThan, String location) {
        this.containsExactWord = containsExactWord;
        this.priceLessThan = priceLessThan;
        this.location = location;
    }

    public String getContainsExactWord() {
        return containsExactWord;
    }

    public void setContainsExactWord(String containsExactWord) {
        this.containsExactWord = containsExactWord;
    }

    public void setPriceLessThan(int priceLessThan) {
        this.priceLessThan = priceLessThan;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPriceLessThan() {
        return priceLessThan;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "SearchBody{" +
                "containsExactWord='" + containsExactWord + '\'' +
                ", priceLessThan=" + priceLessThan +
                ", location='" + location + '\'' +
                '}';
    }
}
