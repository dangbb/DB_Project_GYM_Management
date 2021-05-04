import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class loginException extends SQLException {
    public loginException(String message) {
        super(message);
    }
}

public class loginManagement {
    public static void loginRequest(String username, String password, Connection conn) throws loginException {
        try {
            PreparedStatement ps = conn.prepareStatement("call login(?,?);");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new loginException("Username, password not found.");
            } else {
                accountManagement.login(rs.getString(1), rs.getInt(2), rs.getString(3));
                System.out.println("Login complete, Hello " + rs.getString(1));
            }
        } catch (loginException | accountException e) {
            e.printStackTrace();
            throw new loginException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new loginException("Unable to store login information.");
        }
    }
}
