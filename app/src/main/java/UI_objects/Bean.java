package UI_objects;

/**
 * Created by apple on 15/03/16.
 *
 * //********LISTVIEW************
 */
public class Bean {

    private String image;
    private String title;
    private String discription;
    private String date;
    public String userID;

    public Bean(String image, String title, String discription, String date,String userID) {
        this.image = image;
        this.title = title;
        this.discription = discription;
        this.date = date;
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
