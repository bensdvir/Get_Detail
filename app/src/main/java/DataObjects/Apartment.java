package DataObjects;



public class Apartment {

    private String address;
    private String landLordID;
    private Integer price;
    private Integer floor;
    private Boolean elevator;
    private Integer constructionYear;
    private Boolean wareHouse;
    private String description;
    private Double size;
    private Double averageRank;
    private Boolean parking;
    private Integer numToilet;
    private Integer numRooms;
    private String image;


    public Apartment(){}

    public Apartment(Integer price, Integer floor, Boolean elevator, Integer constructionYear, Boolean wareHouse, String description, Double size, Double averageRank, String address, Boolean parking, Integer numToilet, Integer numRooms, String landLordID, String image) {
        this.setImage(image);
        this.setLandLordID(landLordID);
        this.setPrice(price);
        this.setFloor(floor);
        this.setElevator(elevator);
        this.setConstructionYear(constructionYear);
        this.setWareHouse(wareHouse);
        this.setDescription(description);
        this.setSize(size);
        this.setAvergeRank(averageRank);
        this.setAddress(address);
        this.setParking(parking);
        this.setNumToilet(numToilet);
        this.setNumRooms(numRooms);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Boolean getElevator() {
        return elevator;
    }

    public void setElevator(Boolean elevator) {
        this.elevator = elevator;
    }

    public Integer getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
    }

    public Boolean getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(Boolean wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getAverageRank() {
        return averageRank;
    }

    public void setAvergeRank(Double averageRank) {
        this.averageRank = averageRank;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Integer getNumToilet() {
        return numToilet;
    }

    public void setNumToilet(Integer numToilet) {
        this.numToilet = numToilet;
    }

    public Integer getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(Integer numRooms) {
        this.numRooms = numRooms;
    }

    public String getLandLordID() {
        return landLordID;
    }

    public void setLandLordID(String landLordID) {
        this.landLordID = landLordID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


