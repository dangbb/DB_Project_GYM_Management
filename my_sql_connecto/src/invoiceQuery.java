import java.sql.Connection;
import java.sql.PreparedStatement;

class invoiceQueryException extends Exception {
    public invoiceQueryException(String message) {
        super(message);
    }
}

public class invoiceQuery {
    private final static String insertInvoice = "call insertInvoice(?,?);";
    private final static String insertInvoiceDetail = "call insertInvoiceDetail(?,?)";

    private static PreparedStatement insertInvoiceQuery = null;
    private static PreparedStatement insertInvoiceDetailQuery = null;

    public static void insertInvoice(int staffNo, int memberNo) throws invoiceQueryException {
        try {
            if (insertInvoiceQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            insertInvoiceQuery.setInt(1, staffNo);
            insertInvoiceQuery.setInt(2, memberNo);
            insertInvoiceQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new invoiceQueryException("Unable to insert new invoice.");
        }
    }

    public static void insertInvoiceDetail(String eCode, int sellQuantity) throws invoiceQueryException {
        try {
            if (insertInvoiceDetailQuery == null) {
                setupStatement(mysql_connection.getConn());
            }
            insertInvoiceDetailQuery.setString(1, eCode);
            insertInvoiceDetailQuery.setInt(2, sellQuantity);
            insertInvoiceDetailQuery.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new invoiceQueryException("Unable to insert new invoice detail.");
        }
    }

    private static void setupStatement(Connection conn) throws memberQueryException
    {
        try {
            insertInvoiceQuery = conn.prepareStatement(insertInvoice);
            insertInvoiceDetailQuery = conn.prepareStatement(insertInvoiceDetail);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new memberQueryException("Unable to initiate Statement!");
        }
    }
}
