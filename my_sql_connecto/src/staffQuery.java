import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class staffQueryException extends Exception {
    public staffQueryException(String message)
    {
        super(message);
    }
}

public class staffQuery {
    public static String createStaff = "call insertStaff(?, ?, ?, ?, ?, ?);";
    public static PreparedStatement createStaffQuery = null;

    public static String alterStaff = "call alterStaff(?, ?, ?, ?, ?, ?, ?, ?);";
    public static PreparedStatement alterStaffQuery = null;

    public static String deleteStaff = "call deleteStaff(?);";
    public static PreparedStatement deleteStaffQuery = null;
    ///  ssn, cant be null.

    public static String selectStaff = "call selectStaff();";
    public static PreparedStatement selectStaffQuery = null;

    public static String selectStaffLogin = "call selectStaffLogin(?,?);";
    public static PreparedStatement selectStaffLoginQuery = null;
    /// return list of staff information. exclude username and password.

    public static String selectStaffByMno = "call selectStaffByMno(?);";
    public static PreparedStatement selectStaffByMnoQuery = null;
    
    public static String selectStaffUsername = "call selectStaffUsername(?);";
    public static PreparedStatement selectStaffUsernameQuery = null;

    public static void createStaff(String firstName,
                            String lastName,
                            String phoneNum,
                            String jobtile,
                            String username,
                                   String password) throws staffQueryException {
        try {
            if (createStaffQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createStaffQuery.setString(1, firstName);
            createStaffQuery.setString(2, lastName);
            createStaffQuery.setString(3, phoneNum);
            createStaffQuery.setString(4, jobtile);
            createStaffQuery.setString(5, username);
            createStaffQuery.setString(6, password);
            createStaffQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to create new staff account.");
        }
    }

    public static void deleteStaff(String ssn) throws staffQueryException {
        try {
            deleteStaffQuery.clearParameters();
            deleteStaffQuery.setString(1, ssn);
            if (deleteStaffQuery.execute()) {
                System.out.println("Delete staff complete!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to delete staff");
        }
    }

    public static void alterStaff(String ssn,
                            String firstName,
                            String lastName,
                            String address,
                            String phoneNum,
                            String username) throws staffQueryException {
        try {
            ResultSet rs = selectStaffUsername(username);
            if (rs.next()) {
                throw new staffQueryException("Username already exists.");
            }

            deleteStaff(ssn);
            createStaff(ssn, firstName, lastName, address, phoneNum, username);
            System.out.println("Alter staff account complete!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to alter information");
        }
    }

    public static ResultSet selectStaff(String ssn) throws staffQueryException {
        try {
            selectStaffQuery.clearParameters();
            selectStaffQuery.setString(1, ssn);
            return selectStaffQuery.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to select staff account.");
        }
    }

    public static ResultSet selectStaffByMno(int sno) throws staffQueryException {
        try {
            selectStaffByMnoQuery.clearParameters();
            selectStaffByMnoQuery.setInt(1, sno);
            return selectStaffByMnoQuery.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to select staff account with sno." + sno);
        }
    }

    public static ResultSet selectStaffLogin(String username, String password) throws staffQueryException {
        try {
            selectStaffLoginQuery.setString(1, username);
            selectStaffLoginQuery.setString(2, password);
            return selectStaffLoginQuery.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to select staff login information.");
        }
    }

    public static ResultSet selectStaffUsername(String username) throws staffQueryException {
        try {
            selectStaffUsernameQuery.setString(1, username);
            selectStaffUsernameQuery.clearParameters();
            return selectStaffUsernameQuery.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            throw new staffQueryException("Unable to select staff username information.");
        }
    }

    static void setupStatement(Connection conn) throws staffQueryException
    {
        try {
            createStaffQuery = conn.prepareStatement(createStaff);
            alterStaffQuery = conn.prepareStatement(alterStaff);
            deleteStaffQuery = conn.prepareStatement(deleteStaff);
            selectStaffQuery = conn.prepareStatement(selectStaff);
            selectStaffByMnoQuery = conn.prepareStatement(selectStaffByMno);
            selectStaffLoginQuery = conn.prepareStatement(selectStaffLogin);
            selectStaffUsernameQuery = conn.prepareStatement(selectStaffUsername);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new staffQueryException("Unable to initiate Statement!");
        }
    }
}
