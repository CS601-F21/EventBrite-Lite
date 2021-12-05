package Backend.Servlets.RequestBodyObjects;

public class SearchBody {
    private String containsExactWord;
    private String containsPartialWord;
    private int priceGreaterThan;
    private int priceLessThan;
    private String location;
    private String organizedBy;

    public SearchBody(String containsExactWord, String containsPartialWord, int priceGreaterThan, int priceLessThan, String location, String organizedBy) {
        this.containsExactWord = containsExactWord;
        this.containsPartialWord = containsPartialWord;
        this.priceGreaterThan = priceGreaterThan;
        this.priceLessThan = priceLessThan;
        this.location = location;
        this.organizedBy = organizedBy;
    }

    public String getContainsExactWord() {
        return containsExactWord;
    }

    public void setContainsExactWord(String containsExactWord) {
        this.containsExactWord = containsExactWord;
    }

    public void setContainsPartialWord(String containsPartialWord) {
        this.containsPartialWord = containsPartialWord;
    }

    public void setPriceGreaterThan(int priceGreaterThan) {
        this.priceGreaterThan = priceGreaterThan;
    }

    public void setPriceLessThan(int priceLessThan) {
        this.priceLessThan = priceLessThan;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOrganizedBy(String organizedBy) {
        this.organizedBy = organizedBy;
    }

    public String getContainsPartialWord() {
        return containsPartialWord;
    }

    public int getPriceGreaterThan() {
        return priceGreaterThan;
    }

    public int getPriceLessThan() {
        return priceLessThan;
    }

    public String getLocation() {
        return location;
    }

    public String getOrganizedBy() {
        return organizedBy;
    }
}
