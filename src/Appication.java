import java.awt.*;

public class Appication {
    public static void main(String[] args) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        mainMenu menu = new mainMenu();
        menu.pack();
        menu.setLocation(d.width/4, d.height/4);
        menu.setVisible(false);
        menu.setEnabled(false);

        Login dialog = new Login(menu);
        dialog.setLocation(d.width/4, d.height/4);
        dialog.pack();
        dialog.setVisible(true);

        menu.setLog(dialog);
    }
}
