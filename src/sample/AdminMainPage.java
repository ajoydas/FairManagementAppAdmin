package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminMainPage implements Initializable {
    private static final String url = "jdbc:mysql://162.221.186.242:3306/buetian1_fairinfo";
    private static final String username = "buetian1_ajoy";
    private static final String password = "termjan2016";
    private int key = 0;

    public static String db_name = null;
    public static String fair_name = null;

    Main main;

    public TextArea dbname;
    public TextArea title;
    public TextArea organizer;
    public TextArea location;
    public TextArea startdate;
    public TextArea enddate;
    public TextArea opentime;
    public TextArea closetime;
    public TextArea mapaddress;
    public Button beditfair;
    public Button baddfair;
    public Button bsaveedit;
    public Button bcanceledit;
    public Button baccounts;
    public Button bdeletefair;
    public ListView<String> fairlist;

    ObservableList<Fair> listFairs = FXCollections.observableArrayList();
    ObservableList<String> listFairsName = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        System.out.println("Initializing.......");
        bsaveedit.setVisible(false);
        bcanceledit.setVisible(false);
        baccounts.setVisible(false);
        bdeletefair.setVisible(false);
        beditfair.setVisible(false);
        listFairs = getFairs();
        if (listFairs != null) {
            for (Fair fair : listFairs) {
                listFairsName.add(fair.getTitle());
            }
        }

        fairlist.setItems(listFairsName);
        fairlist.setOnMouseClicked(e -> {
            if (listFairs != null) {
                dbname.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getDb_name());
                title.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getTitle());
                organizer.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getOrganizer());
                location.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getLocation());
                startdate.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getStart_date()));
                enddate.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getEnd_date()));
                opentime.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getOpen_time()));
                closetime.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getClose_time()));
                mapaddress.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getMap_address());
                beditfair.setVisible(true);
            }
        });
    }

    /*class ListenList implements Runnable
    {

        @Override
        public void run() {

        }
    }*/

    ObservableList<Fair> getFairs() {
        ObservableList<Fair> listFairs = FXCollections.observableArrayList();
        String Url = url;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Url, username, password);

            System.out.println("Connected");

            PreparedStatement st = con.prepareStatement("SELECT * FROM fairs");

            System.out.println("Statement");

            ResultSet rs = null, rscount = null;

            rs = st.executeQuery();
            //rscount=st.executeQuery();
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            System.out.println("Count: " + rowcount);

            if (rowcount == 0) {
                System.out.println(rowcount);

                return null;
            } else {
                listFairs = FXCollections.observableArrayList();
                listFairsName = FXCollections.observableArrayList();
                while (rs.next()) {
                    Fair fair = new Fair();
                    fair.setId(rs.getInt("id"));
                    fair.setDb_name(rs.getString("db_name"));
                    fair.setTitle(rs.getString("title"));
                    fair.setOrganizer(rs.getString("organizer"));
                    fair.setLocation(rs.getString("location"));
                    fair.setStart_date(rs.getDate("start_date"));
                    fair.setEnd_date(rs.getDate("end_date"));
                    fair.setOpen_time(rs.getTime("open_time"));
                    fair.setClose_time(rs.getTime("close_time"));
                    fair.setMap_address(rs.getString("map_address"));
                    listFairs.add(fair);
                    System.out.println(fair);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return listFairs;
    }

    public void editFairClicked(ActionEvent actionEvent) {
        //dbname.setEditable(true);
        title.setEditable(true);
        organizer.setEditable(true);
        location.setEditable(true);
        startdate.setEditable(true);
        enddate.setEditable(true);
        opentime.setEditable(true);
        closetime.setEditable(true);
        mapaddress.setEditable(true);

        bsaveedit.setVisible(true);
        bcanceledit.setVisible(true);
        baccounts.setVisible(true);
        bdeletefair.setVisible(true);

        beditfair.setVisible(false);
        baddfair.setVisible(false);
        fairlist.setDisable(true);
        key = 0;
    }

    public void addFairClicked(ActionEvent actionEvent) {
        dbname.setText("");
        title.setText("");
        organizer.setText("");
        location.setText("");
        startdate.setText("");
        enddate.setText("");
        opentime.setText("");
        closetime.setText("");
        mapaddress.setText("");

        dbname.setEditable(true);
        title.setEditable(true);
        organizer.setEditable(true);
        location.setEditable(true);
        startdate.setEditable(true);
        enddate.setEditable(true);
        opentime.setEditable(true);
        closetime.setEditable(true);
        mapaddress.setEditable(true);

        bsaveedit.setVisible(true);
        bcanceledit.setVisible(true);
        baccounts.setVisible(false);
        bdeletefair.setVisible(false);

        beditfair.setVisible(false);
        baddfair.setVisible(false);
        fairlist.setDisable(true);

        key = 1;
    }


    boolean checkFields() {
        if (title.getText().equals("") || location.getText().equals("") || startdate.getText().equals("") || enddate.getText().equals("") || opentime.getText().equals("") || closetime.getText().equals("")) {
            return false;
        }
        try {
            Date sdate = Date.valueOf(startdate.getText());
            Date edate = Date.valueOf(enddate.getText());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            Time otime = Time.valueOf(opentime.getText());
            Time etime = Time.valueOf(closetime.getText());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void saveEditClicked(ActionEvent actionEvent) {

        if (!checkFields()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ENTRY ERROR");
            alert.setHeaderText("Information Can't be Saved");
            alert.setContentText("Please check your entries. Title, Location, Dates and Times must be filled. Dates must be like (yyyy-mm-dd) not(yyyy.mm.dd) or (yyyy/mm/dd) and Times must be like (hh:mm:ss) (24 hr format) and VALID. Please Try Again.");
            alert.getDialogPane().setPrefSize(450, 200);
            alert.showAndWait();
            return;
        }


        if (key == 0) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String Url = url;
                Connection con = DriverManager.getConnection(Url, username, password);

                System.out.println("Connected");
                PreparedStatement st = con.prepareStatement("UPDATE fairs SET title=?,organizer=?,location=?,start_date=?,end_date=?,open_time=?,close_time=?,map_address=? WHERE id=?");

                st.setString(1, title.getText());
                st.setString(2, organizer.getText());
                st.setString(3, location.getText());
                st.setString(4, startdate.getText());
                st.setString(5, enddate.getText());
                st.setString(6, opentime.getText());
                st.setString(7, closetime.getText());
                st.setString(8, mapaddress.getText());
                System.out.println("Id: " + listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getId());
                st.setInt(9, listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getId());

                System.out.println("Statement");

                int rows = st.executeUpdate();

                System.out.println(rows);

                if (rows == 1) {
                    System.out.println("Updated");
                    dbname.setText("");
                    title.setText("");
                    organizer.setText("");
                    location.setText("");
                    startdate.setText("");
                    enddate.setText("");
                    opentime.setText("");
                    closetime.setText("");
                    mapaddress.setText("");

                    title.setEditable(false);
                    organizer.setEditable(false);
                    location.setEditable(false);
                    startdate.setEditable(false);
                    enddate.setEditable(false);
                    opentime.setEditable(false);
                    closetime.setEditable(false);
                    mapaddress.setEditable(false);

                    bsaveedit.setVisible(false);
                    bcanceledit.setVisible(false);
                    baccounts.setVisible(false);
                    bdeletefair.setVisible(false);

                    beditfair.setVisible(true);
                    baddfair.setVisible(true);
                    fairlist.setDisable(false);

                    listFairs = getFairs();

                    listFairsName.clear();
                    for (Fair fair : listFairs) {
                        listFairsName.add(fair.getTitle());
                    }
                    fairlist.setItems(listFairsName);

                    System.out.println("Set....");
                }

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Failed");
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("DATABASE ERROR");
                alert.setHeaderText("Connection Lost");
                alert.setContentText("Please check your connection and try again with valid dataset!");
                alert.getDialogPane().setPrefSize(430, 150);
                alert.showAndWait();
            }
        } else if (key == 1) {
            boolean db = false;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String Url = url;
                Connection con = DriverManager.getConnection(Url, username, password);

                System.out.println("Connected");
                PreparedStatement st = con.prepareStatement("CREATE TABLE " + dbname.getText() + "_employees (\n" +
                        "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  stall VARCHAR(20) DEFAULT NULL,\n" +
                        "  name VARCHAR(100) DEFAULT NULL,\n" +
                        "  description VARCHAR(255) DEFAULT NULL,\n" +
                        "  contact_no VARCHAR(255) DEFAULT NULL,\n" +
                        "  position VARCHAR(50) DEFAULT NULL,\n" +
                        "  salary VARCHAR(20) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ")");
                boolean rows = st.execute();
                st = con.prepareStatement(
                        "CREATE TABLE " + dbname.getText() + "_products (\n" +
                                "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  stall VARCHAR(50) DEFAULT NULL,\n" +
                                "  name VARCHAR(255) DEFAULT NULL,\n" +
                                "  company VARCHAR(255) DEFAULT NULL,\n" +
                                "  description VARCHAR(500) DEFAULT NULL,\n" +
                                "  price VARCHAR(25) NOT NULL,\n" +
                                "  availability VARCHAR(15) DEFAULT NULL,\n" +
                                "  image LONGBLOB,\n" +
                                "  PRIMARY KEY (id)\n" +
                                ")");
                rows = st.execute();
                st = con.prepareStatement(
                        "CREATE TABLE " + dbname.getText() + "_sells (\n" +
                                "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  stall VARCHAR(20) DEFAULT NULL,\n" +
                                "  product_name VARCHAR(255) DEFAULT NULL,\n" +
                                "  employee_name VARCHAR(100) DEFAULT NULL,\n" +
                                "  date VARCHAR(50) DEFAULT NULL,\n" +
                                "  time VARCHAR(50) DEFAULT NULL,\n" +
                                "  price VARCHAR(50) DEFAULT NULL,\n" +
                                "  description VARCHAR(255) DEFAULT NULL,\n" +
                                "  PRIMARY KEY (id)\n" +
                                ")");
                rows = st.execute();
                st = con.prepareStatement(
                        "CREATE TABLE " + dbname.getText() + "_stalls (\n" +
                                "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  stall VARCHAR(50) DEFAULT NULL,\n" +
                                "  stall_name VARCHAR(255) DEFAULT NULL,\n" +
                                "  owner VARCHAR(255) DEFAULT NULL,\n" +
                                "  description VARCHAR(255) DEFAULT NULL,\n" +
                                "  location VARCHAR(255) DEFAULT NULL,\n" +
                                "  PRIMARY KEY (id)\n" +
                                ") ");
                rows = st.execute();
                st = con.prepareStatement(
                        "CREATE TABLE " + dbname.getText() + "_users (\n" +
                                "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                                "  username VARCHAR(255) DEFAULT NULL,\n" +
                                "  password VARCHAR(255) DEFAULT NULL,\n" +
                                "  mobile_no BIGINT(11) DEFAULT NULL,\n" +
                                "  PRIMARY KEY (id)\n" +
                                ")");
                rows = st.execute();
                con.close();
                con = DriverManager.getConnection(Url, username, password);
                st = con.prepareStatement("INSERT INTO fairs" +
                        "(db_name,title,organizer,location,start_date,end_date,open_time,close_time,map_address)" +
                        "VALUES" +
                        "(?,?,?,?,?,?,?,?,?)");

                st.setString(1, dbname.getText());
                st.setString(2, title.getText());
                st.setString(3, organizer.getText());
                st.setString(4, location.getText());
                st.setString(5, startdate.getText());
                st.setString(6, enddate.getText());
                st.setString(7, opentime.getText());
                st.setString(8, closetime.getText());
                st.setString(9, mapaddress.getText());
                System.out.println("Statement");

                rows = st.execute();

                System.out.println(rows);

                    if (!rows) {
                        System.out.println("Created");
                        dbname.setText("");
                        title.setText("");
                        organizer.setText("");
                        location.setText("");
                        startdate.setText("");
                        enddate.setText("");
                        opentime.setText("");
                        closetime.setText("");
                        mapaddress.setText("");
                    dbname.setEditable(false);
                    bsaveedit.setVisible(false);
                    bcanceledit.setVisible(false);
                    baccounts.setVisible(false);
                    bdeletefair.setVisible(false);

                    beditfair.setVisible(true);
                    baddfair.setVisible(true);
                    fairlist.setDisable(false);

                    listFairs = getFairs();

                    listFairsName.clear();
                    for (Fair fair : listFairs) {
                        listFairsName.add(fair.getTitle());
                    }
                    fairlist.setItems(listFairsName);

                }

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Failed");
                e.printStackTrace();
                /*if (db) {
                    System.out.println("Deleting Database");
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection(url, username, password);
                        PreparedStatement st = con.prepareStatement("DROP DATABASE " + dbname.getText() + " ;\n");//
                        boolean rows = st.execute();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
*/
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("DATABASE ERROR");
                alert.setHeaderText("Connection Lost.");
                alert.setContentText("Please check your connection or try using different database name with valid dataset!");
                alert.getDialogPane().setPrefSize(430, 150);
                alert.showAndWait();

            }
        }

    }

    public void cancelEditClicked(ActionEvent actionEvent) {
        dbname.setEditable(false);
        title.setEditable(false);
        organizer.setEditable(false);
        location.setEditable(false);
        startdate.setEditable(false);
        enddate.setEditable(false);
        opentime.setEditable(false);
        closetime.setEditable(false);
        mapaddress.setEditable(false);

        bsaveedit.setVisible(false);
        bcanceledit.setVisible(false);
        baccounts.setVisible(false);
        bdeletefair.setVisible(false);
        baddfair.setVisible(true);
        fairlist.setDisable(false);
        if (key == 0) {
            dbname.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getDb_name());
            title.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getTitle());
            organizer.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getOrganizer());
            location.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getLocation());
            startdate.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getStart_date()));
            enddate.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getEnd_date()));
            opentime.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getOpen_time()));
            closetime.setText(String.valueOf(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getClose_time()));
            mapaddress.setText(listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getMap_address());
            beditfair.setVisible(true);
        } else if (key == 1) {
            fairlist.getSelectionModel().clearSelection();
            dbname.setText("");
            title.setText("");
            organizer.setText("");
            location.setText("");
            startdate.setText("");
            enddate.setText("");
            opentime.setText("");
            closetime.setText("");
            mapaddress.setText("");
        }
    }

    public void deleteFairClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ARE YOU SURE?");
        alert.setContentText("Are you sure you want to delete the fair?");
        alert.getDialogPane().setPrefSize(430, 150);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Pressed Cancel");
            return;
        }

        System.out.println("Deleting Database");
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            PreparedStatement st = con.prepareStatement("DROP TABLE " + dbname.getText() + "_employees ;\n");
            boolean rows = st.execute();
            st = con.prepareStatement("DROP TABLE " + dbname.getText() + "_products ;\n");
            rows = st.execute();
            st = con.prepareStatement("DROP TABLE " + dbname.getText() + "_sells ;\n");
            rows = st.execute();

            st = con.prepareStatement("DROP TABLE " + dbname.getText() + "_stalls ;\n");
            rows = st.execute();
            st = con.prepareStatement("DROP TABLE " + dbname.getText() + "_users ;\n");
            rows = st.execute();

            st = con.prepareStatement("DELETE FROM fairs WHERE db_name=?");
            st.setString(1, dbname.getText());
            rows = st.execute();

            if(!rows)
            {
                dbname.setText("");
                title.setText("");
                organizer.setText("");
                location.setText("");
                startdate.setText("");
                enddate.setText("");
                opentime.setText("");
                closetime.setText("");
                mapaddress.setText("");
                dbname.setEditable(false);
                bsaveedit.setVisible(false);
                bcanceledit.setVisible(false);
                baccounts.setVisible(false);
                bdeletefair.setVisible(false);

                beditfair.setVisible(true);
                baddfair.setVisible(true);
                fairlist.setDisable(false);

                listFairs = getFairs();
                if(listFairs!=null) {
                    listFairsName.clear();
                    for (Fair fair : listFairs) {
                        listFairsName.add(fair.getTitle());
                    }
                    fairlist.setItems(listFairsName);
                }
                else
                {
                    fairlist.getSelectionModel().clearSelection();
                    fairlist.getItems().clear();
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DATABASE ERROR");
            alert.setHeaderText("Connection Lost or Database Name Matched");
            alert.setContentText("Please check your connection and Try again!");
            alert.getDialogPane().setPrefSize(430, 150);
            alert.showAndWait();
        }
    }

    public void accountClicked(ActionEvent actionEvent) {
        try {
            db_name = listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getDb_name();
            fair_name = listFairs.get(fairlist.getSelectionModel().getSelectedIndex()).getTitle();
            main.accountPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setMain(Main main) {
        this.main = main;
    }
}
