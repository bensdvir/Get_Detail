package DataObjects;


public class Comment {

    private String address;
    private String userToken;
    private String text;
    private String timeStamp;
    private String userPictureUrl;
    private String userName;


    public Comment(){}

    public Comment(String address,String userID,String text,String timeStamp,String userPictureUrl,String userNameS) {
        this.setAddress(address);
        this.setUserToken(userID);
        this.setText(text);
        this.setTimeStamp(timeStamp);
        this.setUserPictureUrl(userPictureUrl);
        this.setUserName(userName);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserPictureUrl() {
        return userPictureUrl;
    }

    public void setUserPictureUrl(String userPictureUrl) {
        this.userPictureUrl = userPictureUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}


