import java.sql.*;

public class mysql_connection {
    static private final String jdbc_drive = "com.mysql.jdbc.Driver";
    static private final String db_url = "jdbc:mysql://localhost/";

    static private final String USER = "root";
    static private final String PASSWORD = "";

    static private final String CURRENT_DATABASE = "equipment_management";

    static private Connection conn = null;
    static private Statement stmt = null;
    static private ResultSet rs = null;

    static private boolean connected = false;

    public static Connection getConn() {
        if (conn == null) {
            setupConnection();
        }
        return conn;
    }

    public static String getCurrentDatabase() {
        return CURRENT_DATABASE;
    }

    public static boolean setupConnection() {
        try {
            System.out.print("-> Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(db_url, USER, PASSWORD);

            System.out.println("** Connection Establish complete!! **");

            stmt = conn.createStatement();
            connected = true;

            useDatabase(CURRENT_DATABASE);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("** Connection failed! ");
        }
        return false;
    }

    public static void useDatabase(String database_name) {
        try {
            System.out.printf("-> Using database %s **%n", database_name);
            rs = stmt.executeQuery(String.format("use %s", database_name));

            System.out.printf("** Use %s complete **", database_name);
            System.out.println();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("** Using database failed! ");
        }
    }

    public static void main(String[] args) {
        try {
            setupConnection();
            staffQuery.setupStatement(conn);

            PreparedStatement ps = conn.prepareStatement("call login(?,?);");
            ps.setString(1, "yenes");
            ps.setString(2, "456");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1) + rs.getInt(2) + rs.getString(3));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
