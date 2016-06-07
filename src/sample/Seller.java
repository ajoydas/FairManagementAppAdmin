package sample;

/**
 * Created by ajoy on 6/5/16.
 */
public class Seller {
    private int id;
    private String username;
    private String password;
    private long mobileno;
    private String latlng;

    public Seller(int id, String username, String password, long mobileno, String latlng) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mobileno = mobileno;
        this.latlng = latlng;
    }

    public Seller() {

    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMobileno() {
        return mobileno;
    }

    public void setMobileno(long mobileno) {
        this.mobileno = mobileno;
    }
}
