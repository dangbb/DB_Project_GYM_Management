import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class importEquipmentException extends Exception {
    public importEquipmentException(String message)
    {
        super(message);
    }
}

public class importEquipmentQuery {
    public static String createImportReport = "call insertImport(?);";
    public static PreparedStatement createImportReportQuery = null;

    public static String createImportDetail = "call insertImportDetail(?,?,?);";
    public static PreparedStatement createImportDetailQuery = null;

    public static void insertImport(int staffNo) throws importEquipmentException {
        try {
            if (createImportReportQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createImportReportQuery.setInt(1, staffNo);
            createImportReportQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new importEquipmentException("Unable to insert record.");
        }
    }

    public static void insertImportDetail(String eCode,
                                          int importQuantity,
                                          float priceEach) throws importEquipmentException {
        try {
            if (createImportDetailQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            createImportDetailQuery.setString(1, eCode);
            createImportDetailQuery.setInt(2, importQuantity);
            createImportDetailQuery.setFloat(3, priceEach);
            createImportDetailQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new importEquipmentException("Unable to insert record.");
        }
    }

    private static void setupStatement(Connection conn) throws importEquipmentException
    {
        try {
            createImportReportQuery = conn.prepareStatement(createImportReport);
            createImportDetailQuery = conn.prepareStatement(createImportDetail);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new importEquipmentException("Unable to initiate Statement!");
        }
    }
}
