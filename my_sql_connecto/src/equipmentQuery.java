import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

class equipmentQueryException extends Exception {
    public equipmentQueryException(String message)
    {
        super(message);
    }
}

class equipment {
    private String eCode;
    private String eName;
    private boolean eIsSellable;
    private float unitPrice;

    public equipment (String eCode, String eName, boolean eIsSellable, float unitPrice) {
        this.eCode = eCode;
        this.eName = eName;
        this.eIsSellable = eIsSellable;
        this.unitPrice = unitPrice;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public String geteCode() {
        return eCode;
    }

    public String geteName() {
        return eName;
    }

    public boolean iseIsSellable() {
        return eIsSellable;
    }
}

public class equipmentQuery {
    public static String createEquipment = "call insertEquipment(?,?,?,?);";
    public static PreparedStatement createEquipmentQuery = null;

    public static String getPrice = "call getPrice(?);";
    public static PreparedStatement getPriceQuery = null;

    public static String selectEquipment = "call getEquipment(?);";
    public static PreparedStatement selectEquipmentQuery = null;

    public static String getRemain = "call getRemain(?);";
    public static PreparedStatement getRemainQuery = null;

    public static String findEquipmentByNameStr = "call findEquipmentByName(?);";
    public static PreparedStatement findEquipmentByNameQuery = null;

    public static void insertEquipment(
                                        String eCode,
                                        String eName,
                                        boolean eIsForRent,
                                        float rentFee) throws equipmentQueryException {
        try {
            if (createEquipmentQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createEquipmentQuery.setString(1, eCode);
            createEquipmentQuery.setString(2, eName);
            createEquipmentQuery.setBoolean(3, eIsForRent);
            createEquipmentQuery.setFloat(4, rentFee);
            if (createEquipmentQuery.execute()) {
                System.out.println("Insert new equipment complete!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new equipmentQueryException("Unable to create new equipment.");
        }
    }

    public static float getEquipmentPrice(String eCode) throws equipmentQueryException {
        try {
            if (getPriceQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            getPriceQuery.setString(1, eCode);
            ResultSet rs = getPriceQuery.executeQuery();
            if (!rs.next()) {
                throw new equipmentQueryException("Unable to find equipment with code.");
            }
            return rs.getFloat(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new equipmentQueryException("Unable to get price for equipment.");
        }
    }

    public static ResultSet getEquipment(String eCode) throws equipmentQueryException {
        try {
            if (selectEquipmentQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            selectEquipmentQuery.setString(1, eCode);
            ResultSet rs = selectEquipmentQuery.executeQuery();
            if (!rs.next()) {
                throw new equipmentQueryException("Cannot find equipment with corresponding code.");
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new equipmentQueryException("Unable to find equipment with Code.");
        }
    }

    public static int getRemain(String eCode) throws equipmentQueryException {
        try {
            if (getRemainQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            getRemainQuery.setString(1, eCode);
            ResultSet rs = getRemainQuery.executeQuery();
            if (!rs.next()) {
                throw new equipmentQueryException("Cannot find equipment remain with corresponding code.");
            }
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new equipmentQueryException("Unable to find equipment remain with Code.");
        }
    }

    public static Vector<equipment> findEquipmentByName(String name) throws equipmentQueryException {
        try {
            Vector<equipment> result = new Vector<>();
            if (findEquipmentByNameQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            findEquipmentByNameQuery.setString(1, name);
            ResultSet rs = findEquipmentByNameQuery.executeQuery();

            while (rs.next()) {
                result.add(new equipment(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getBoolean(3),
                        rs.getFloat(4)
                ));
            }

            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new equipmentQueryException("Unable to find equipment by name.");
        }
    }

    private static void setupStatement(Connection conn) throws equipmentQueryException
    {
        try {
            createEquipmentQuery = conn.prepareStatement(createEquipment);
            getPriceQuery = conn.prepareStatement(getPrice);
            selectEquipmentQuery = conn.prepareStatement(selectEquipment);
            getRemainQuery = conn.prepareStatement(getRemain);
            findEquipmentByNameQuery = conn.prepareStatement(findEquipmentByNameStr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new equipmentQueryException("Unable to initiate Statement!");
        }
    }
}
