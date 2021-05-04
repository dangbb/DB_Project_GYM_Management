import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField usernamePath;
    private JPasswordField passwordPath;
    private JButton submitPath;
    private JTextArea comment;
    private JPanel panel;
    private JPanel LargeForm;
    private JLabel title;
    private mainMenu menu;

    public void setActive(boolean active) {
        this.setVisible(active);
        this.setEnabled(active);

        if (active) {
            submitPath.setEnabled(true);
            comment.setText("");
        }
    }

    public Login(mainMenu menu) {
        this.menu = menu;
        setContentPane(LargeForm);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(submitPath);
        setVisible(true);

        title.setFont(new Font("Arial", Font.PLAIN, 20));


        submitPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submitPath) {
                    String username = usernamePath.getText();
                    String password = String.valueOf(passwordPath.getPassword());

                    usernamePath.setText("");
                    passwordPath.setText("");

                    try {
                        loginManagement.loginRequest(username, password, mysql_connection.getConn());
                        System.out.println(username + " " + password);
                        comment.setText("Hello, " + accountManagement.getName());

                        submitPath.setEnabled(false);

                        if (menu != null) {
                            menu.setActive(true);
                            setActive(false);
                        }

                    } catch (SQLException exc) {
                        exc.printStackTrace();
                        comment.setText(exc.getMessage());
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws staffQueryException {
        mysql_connection.setupConnection();

        Login dialog = new Login(null);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }
}
