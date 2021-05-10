import java.sql.*;

class memberQueryException extends Exception {
    public memberQueryException(String message)
    {
        super(message);
    }
}

public class memberQuery {
    public static String createMember = "call insertMember(?,?,?);";
    public static PreparedStatement createMemberQuery = null;

    public static String getMember = "call getMember(?,?,?);";
    public static PreparedStatement getMemberQuery = null;

    public static String getMemberByMemberNo = "call getMemberByMemberNo(?);";
    public static PreparedStatement getMemberByMemberNoQuery = null;

    public static int createMember(String firstName,
                                    String lastName,
                                    String phoneNum) throws memberQueryException {
        try {
            if (createMemberQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createMemberQuery.setString(1, firstName);
            createMemberQuery.setString(2, lastName);
            createMemberQuery.setString(3, phoneNum);
            ResultSet memberNumber = createMemberQuery.executeQuery();
            memberNumber.next();
            return memberNumber.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new memberQueryException("Unable to create new member record.");
        }
    }

    public static ResultSet getMember(String fname,
                                 String lname,
                                 String phoneNum) throws memberQueryException {
        try {
            if (getMemberQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            getMemberQuery.setString(1, fname);
            getMemberQuery.setString(2, lname);
            getMemberQuery.setString(3, phoneNum);
            ResultSet rs = getMemberQuery.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new memberQueryException("Unable to perform mySQL query.");
        }
    }

    public static ResultSet getMemberByMemberNo(int memberNo) throws memberQueryException {
        try {
            if (getMemberByMemberNoQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            getMemberByMemberNoQuery.setInt(1, memberNo);
            ResultSet rs = getMemberByMemberNoQuery.executeQuery();
            rs.next();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            throw new memberQueryException("Unable to perform query in mySQL.");
        }
    }

    private static void setupStatement(Connection conn) throws memberQueryException
    {
        try {
            createMemberQuery = conn.prepareStatement(createMember);
            getMemberQuery = conn.prepareStatement(getMember);
            getMemberByMemberNoQuery = conn.prepareStatement(getMemberByMemberNo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new memberQueryException("Unable to initiate Statement!");
        }
    }
}
