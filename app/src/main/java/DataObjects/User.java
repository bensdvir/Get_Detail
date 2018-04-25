package DataObjects;

public class User {

    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String token;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private Double avgRankLandLoard;
    private Double avgRankRanker;
    private String image;

    public User(){}

    public User(String token, String firstName, String lastName, String gender, String email, Double avgRankLandLoard, Double avgRankRanker, String image) {
        this.setToken(token);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setGender(gender);
        this.setEmail(email);
        this.setAvgRankLandLoard(avgRankLandLoard);
        this.setAvgRankRanker(avgRankRanker);
        this.setImage(image);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAvgRankLandLoard() {
        return avgRankLandLoard;
    }

    public void setAvgRankLandLoard(Double avgRankLandLoard) {
        this.avgRankLandLoard = avgRankLandLoard;
    }

    public Double getAvgRankRanker() {
        return avgRankRanker;
    }

    public void setAvgRankRanker(Double avgRankRanker) {
        this.avgRankRanker = avgRankRanker;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

