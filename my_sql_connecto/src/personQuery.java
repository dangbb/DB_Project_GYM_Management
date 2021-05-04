import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class personQueryException extends Exception {
    public personQueryException(String message)
    {
        super(message);
    }
}

public class personQuery {
    public static String insertPerson = "call insertPerson(?, ?, ?);";
    public static PreparedStatement insertPersonQuery = null;

    public static void insertPerson(String firstName,
                       String lastName,
                       String phoneNum) throws personQueryException {
        try {
            if (insertPersonQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            insertPersonQuery.clearParameters();
            insertPersonQuery.setString(1, firstName);
            insertPersonQuery.setString(2, lastName);
            insertPersonQuery.setString(3, phoneNum);
            insertPersonQuery.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new personQueryException("Unable to insert new record");
        }
    }

    public static void setupStatement(Connection conn) throws personQueryException
    {
        try {
            insertPersonQuery = conn.prepareStatement(insertPerson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new personQueryException("Unable to initiate Statement!");
        }
    }
}
