import java.util.UUID;

public class Address {
    private UUID id;
    private String city;
    private String street;
    private String house;

    public Address() {

    }

    public Address(UUID id, String city, String street, String house) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
}
