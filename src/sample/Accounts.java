package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by ajoy on 6/5/16.
 */
public class Accounts implements Initializable {
    private static final String url = "jdbc:mysql://162.221.186.242:3306/buetian1_fairinfo";
    private static final String username = "buetian1_ajoy";
    private static final String password = "termjan2016";
    public Button bback;
    public TextField latlng;

    private String db_name = null;
    private String fair_name = null;

    Main main;
    int key = 0;

    public ListView<String> accountlist;
    public TextField user;
    public TextField editpass;
    public PasswordField pass;
    public TextField contact;
    public Button bedit;
    public Button badd;
    public Button bsaveedit;
    public Button bcanceledit;
    public Button bdelete;
    public Label fairname;

    ObservableList<Seller> listSeller = FXCollections.observableArrayList();
    ObservableList<String> listUsername = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listUsername = null;
        listSeller = null;

        db_name = AdminMainPage.db_name;
        fair_name = AdminMainPage.fair_name;
        fairname.setText(fair_name);
        editpass.setVisible(false);
        bsaveedit.setVisible(false);
        bcanceledit.setVisible(false);
        bdelete.setVisible(false);
        bedit.setVisible(false);
        listSeller = getSellers();
        if(listSeller!=null) {
            for (Seller seller : listSeller) {
                listUsername.add(seller.getUsername());
            }
        }
        accountlist.setItems(listUsername);
        accountlist.setOnMouseClicked(e -> {
            if(listSeller!=null) {
                user.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getUsername());
                pass.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getPassword());
                contact.setText(String.valueOf(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getMobileno()));
                latlng.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getLatlng());
                bedit.setVisible(true);
            }
        });


    }

    ObservableList<Seller> getSellers() {
        ObservableList<Seller> listSellers = FXCollections.observableArrayList();
        String Url = url;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Url, username, password);

            System.out.println("Connected");

            PreparedStatement st = con.prepareStatement("SELECT * FROM "+ db_name+"_users");

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
            }

            listSeller = FXCollections.observableArrayList();
            listUsername = FXCollections.observableArrayList();
            while (rs.next()) {
                Seller seller = new Seller();
                seller.setId(rs.getInt("id"));
                seller.setUsername(rs.getString("username"));
                seller.setPassword(rs.getString("password"));
                seller.setMobileno(rs.getLong("mobile_no"));

                PreparedStatement newst = con.prepareStatement("SELECT * FROM "+ db_name+"_stalls WHERE stall=?");
                newst.setString(1, seller.getUsername());
                ResultSet newrs = newst.executeQuery();
                //rscount=st.executeQuery();
                int rows = 0;
                if (newrs.last()) {
                    rows = newrs.getRow();
                    newrs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                }

                System.out.println("Count: " + rowcount);

                if (rows == 0) {
                    System.out.println(rows);
                    return null;
                }
                while (newrs.next()) {
                    seller.setLatlng(newrs.getString("location"));
                }

                listSellers.add(seller);
                System.out.println(seller);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }

        return listSellers;
    }


    private boolean checkFields() {
        try {
            long mobile = Long.parseLong(contact.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        if (user.getText().equals("") || user.getText() == null || editpass.getText().equals("") || editpass.getText() == null) {
            return false;
        }

        if(contact.getText().length()>11)
        {
            return false;
        }
        return true;
    }

    private boolean checkUser() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);

            System.out.println("Connected");

            PreparedStatement st = con.prepareStatement("SELECT * FROM "+ db_name+"_users WHERE username=?");
            st.setString(1, user.getText());
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
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void editClicked(ActionEvent actionEvent) {
        key = 0;
        user.setEditable(false);
        pass.setEditable(true);
        contact.setEditable(true);
        latlng.setEditable(true);

        bsaveedit.setVisible(true);
        bcanceledit.setVisible(true);
        bdelete.setVisible(true);
        editpass.setVisible(true);
        accountlist.setDisable(true);

        pass.setVisible(false);
        bedit.setVisible(false);
        badd.setVisible(false);

        editpass.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getPassword());
    }

    public void addClicked(ActionEvent actionEvent) {
        key = 1;
        user.setEditable(true);
        pass.setEditable(true);
        contact.setEditable(true);
        latlng.setEditable(true);

        bsaveedit.setVisible(true);
        bcanceledit.setVisible(true);
        editpass.setVisible(true);
        pass.setVisible(false);
        bedit.setVisible(false);
        badd.setVisible(false);
        accountlist.setDisable(true);

        user.setText("");
        editpass.setText("");
        contact.setText("");
        latlng.setText("");
    }

    public void saveEditClicked(ActionEvent actionEvent) {
        if (!checkFields()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ENTRY ERROR");
            alert.setHeaderText("Information Can't be Saved");
            alert.setContentText("Please check your entries. Contact No must be a number less or equals 11 digits.Username and Password cant be empty.Please Try Again.");
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
                PreparedStatement st = con.prepareStatement("UPDATE "+ db_name+"_users SET password=?,mobile_no=? WHERE id=?");

                st.setString(1, editpass.getText());
                st.setString(2, contact.getText());
                st.setInt(3, listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getId());

                System.out.println("Statement");

                int rows = st.executeUpdate();

                System.out.println(rows);

                if (rows == 1) {
                    PreparedStatement newst = con.prepareStatement("UPDATE "+ db_name+"_stalls SET location=? WHERE stall=?");
                    newst.setString(1, latlng.getText());
                    newst.setString(2, user.getText());
                    int rownum = newst.executeUpdate();
                    //rscount=st.executeQuery();
                    if (rownum == 0) {
                        System.out.println(rows);
                        return;
                    }

                    System.out.println("Updated");
                    user.setText("");
                    pass.setText("");
                    editpass.setText("");
                    contact.setText("");
                    latlng.setText("");

                    pass.setVisible(true);
                    user.setEditable(false);
                    contact.setEditable(false);
                    latlng.setEditable(false);
                    bsaveedit.setVisible(false);
                    bcanceledit.setVisible(false);
                    editpass.setVisible(false);
                    bdelete.setVisible(false);

                    bedit.setVisible(true);
                    badd.setVisible(true);
                    accountlist.setDisable(false);

                    listSeller = getSellers();

                    listUsername.clear();
                    for (Seller seller : listSeller) {
                        listUsername.add(seller.getUsername());
                    }

                    accountlist.setItems(listUsername);
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
            boolean userEntry = false;
            if (!checkUser()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("USER ENTRY");
                alert.setHeaderText("Username matched!");
                alert.setContentText("Username already exists. Please try again by changing it.");
                alert.getDialogPane().setPrefSize(430, 150);
                alert.showAndWait();
                return;
            }

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String Url = url;
                Connection con = DriverManager.getConnection(Url, username, password);

                System.out.println("Connected");
                PreparedStatement st = con.prepareStatement("INSERT INTO "+ db_name+"_users" +
                        "(username,password,mobile_no)" +
                        "VALUES" +
                        "(?,?,?)");

                st.setString(1, user.getText());
                st.setString(2, editpass.getText());
                st.setString(3, contact.getText());

                boolean rows = st.execute();

                System.out.println(rows);

                if (!rows) {
                    userEntry = true;
                    PreparedStatement newst = con.prepareStatement("INSERT INTO "+ db_name+"_stalls" +
                            "(stall,stall_name,owner,description,location)" +
                            "VALUES" +
                            "(?,?,?,?,?)");
                    newst.setString(1, user.getText());
                    newst.setString(2, user.getText());
                    newst.setString(3, user.getText());
                    newst.setString(4, user.getText());
                    newst.setString(5, latlng.getText());
                    boolean rownum = newst.execute();

                    if (rownum) {
                        System.out.println(rownum);
                        return;
                    }


                    System.out.println("Created");
                    user.setText("");
                    pass.setText("");
                    editpass.setText("");
                    contact.setText("");
                    latlng.setText("");

                    user.setEditable(false);
                    pass.setVisible(true);
                    contact.setEditable(false);
                    latlng.setEditable(false);

                    bsaveedit.setVisible(false);
                    bcanceledit.setVisible(false);
                    editpass.setVisible(false);
                    bedit.setVisible(true);
                    badd.setVisible(true);
                    accountlist.setDisable(false);

                    listSeller = getSellers();


                    if(listUsername!=null)listUsername.clear();

                    for (Seller seller : listSeller) {
                        System.out.println(seller);

                        listUsername.add(seller.getUsername());
                    }

                    accountlist.setItems(listUsername);

                }
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Failed");
                e.printStackTrace();
                if (userEntry) {
                    System.out.println("Deleting User");
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection(url, username, password);
                        PreparedStatement st = con.prepareStatement("DELETE FROM "+ db_name+"_users WHERE username=?");
                        st.setString(1, user.getText());
                        boolean rows = st.execute();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("DATABASE ERROR");
                alert.setHeaderText("Connection Lost or Database Name Matched");
                alert.setContentText("Please check your connection or try using different database name with valid dataset!");
                alert.getDialogPane().setPrefSize(430, 150);
                alert.showAndWait();

            }
        }

    }

    public void cancelEditClicked(ActionEvent actionEvent) {
        user.setEditable(false);
        pass.setVisible(true);
        contact.setEditable(false);

        bsaveedit.setVisible(false);
        bcanceledit.setVisible(false);
        editpass.setVisible(false);
        bedit.setVisible(true);
        badd.setVisible(true);
        accountlist.setDisable(false);

        if (key == 0) {
            user.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getUsername());
            pass.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getPassword());
            contact.setText(String.valueOf(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getMobileno()));
            latlng.setText(listSeller.get(accountlist.getSelectionModel().getSelectedIndex()).getLatlng());

        } else if (key == 1) {
            accountlist.getSelectionModel().clearSelection();
            user.setText("");
            editpass.setText("");
            contact.setText("");
            latlng.setText("");
        }
    }

    public void deleteClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ARE YOU SURE?");
        alert.setContentText("Are you sure you want to delete the account and associate data?");
        alert.getDialogPane().setPrefSize(430, 150);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Pressed Cancel");
            return;
        }
        System.out.println("Deleting Database");
        Connection con = null;
        boolean data = false;
        try {
            con = DriverManager.getConnection(url, username, password);
            PreparedStatement st = con.prepareStatement("DELETE FROM "+ db_name+"_sells WHERE stall=?");
            st.setString(1, user.getText());
            boolean rows = st.execute();
            data = true;
            st = con.prepareStatement("DELETE FROM "+ db_name+"_employees WHERE stall=?");
            st.setString(1, user.getText());
            rows = st.execute();

            st = con.prepareStatement("DELETE FROM "+ db_name+"_products WHERE stall=?");
            st.setString(1, user.getText());
            rows = st.execute();

            st = con.prepareStatement("DELETE FROM "+ db_name+"_stalls WHERE stall=?");
            st.setString(1, user.getText());
            rows = st.execute();

            st = con.prepareStatement("DELETE FROM "+ db_name+"_users WHERE username=?");
            st.setString(1, user.getText());
            rows = st.execute();

            data = false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DATABASE ERROR");
            alert.setHeaderText("Connection Lost .");
            alert.setContentText("Please check your connection and Try again!");
            alert.getDialogPane().setPrefSize(430, 150);
            alert.showAndWait();
        }

        if(data)
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DATABASE ERROR");
            alert.setHeaderText("Connection lost while deleting. Data related to the account may have beeen damaged!");
            alert.setContentText("Please check your connection and Try again!");
            alert.getDialogPane().setPrefSize(430, 150);
            alert.showAndWait();
        }

        user.setEditable(false);
        pass.setVisible(true);
        contact.setEditable(false);

        bsaveedit.setVisible(false);
        bcanceledit.setVisible(false);
        editpass.setVisible(false);
        bedit.setVisible(true);
        badd.setVisible(true);
        accountlist.setDisable(false);

        listSeller = getSellers();

        if(listSeller!=null) {
            if (listUsername != null) listUsername.clear();

            for (Seller seller : listSeller) {
                System.out.println(seller);

                listUsername.add(seller.getUsername());
            }
            accountlist.setItems(listUsername);
        }
        else
        {
            accountlist.getSelectionModel().clearSelection();
            accountlist.getItems().clear();
        }

    }


    public void setMain(Main main) {
        this.main = main;
    }

    public void backClicked(ActionEvent actionEvent) {
        try {
            main.adminMainPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
