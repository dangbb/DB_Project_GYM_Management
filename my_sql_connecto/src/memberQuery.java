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

    public static void createMember(String firstName,
                                    String lastName,
                                    String phoneNum) throws memberQueryException {
        try {
            if (createMemberQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createMemberQuery.setString(1, firstName);
            createMemberQuery.setString(2, lastName);
            createMemberQuery.setString(3, phoneNum);
            if (createMemberQuery.execute()) {
                System.out.println("Insert complete!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new memberQueryException("Unable to create new member record.");
        }
    }

    private static void setupStatement(Connection conn) throws memberQueryException
    {
        try {
            createMemberQuery = conn.prepareStatement(createMember);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new memberQueryException("Unable to initiate Statement!");
        }
    }
}
