class accountException extends Exception {
    public accountException(String message) {
        super(message);
    }
}

public class accountManagement {
    private static String name;
    private static int staffNo;
    private static String jobtitle;
    private static boolean isLogin = false;

    public static void login(String name, int staffNo, String jobtitle) throws accountException {
        if (isLogin) {
            throw new accountException("Account haven't logout yet.");
        } else {
            accountManagement.name = name;
            accountManagement.staffNo = staffNo;
            accountManagement.jobtitle = jobtitle;
            isLogin = true;
        }
    }

    public static void logout() throws accountException {
        if (isLogin) {
            isLogin = false;
        } else {
            throw new accountException("Account haven't login yet.");
        }
    }

    public static String getName() {
        if (!isLogin) return null;
        return name;
    }

    public static int getStaffNo() {
        if (!isLogin) return 0;
        return staffNo;
    }

    public static String getJobtitle() {
        if (!isLogin) return null;
        return jobtitle;
    }

    public static void main(String[] args) {
        try {
            login("dangbb", 1,"Manager");
            System.out.println("Account name " + getName() + " Staff no " + getStaffNo() + " job title " + getJobtitle());
            logout();

            System.out.println("Account name " + getName() + " Staff no " + getStaffNo() + " job title " + getJobtitle());

        } catch (accountException e) {
            e.printStackTrace();
        }
    }
}
