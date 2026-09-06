package vn.iotstar.constants;

public class Constant {

    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";
    public static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    public static class Path {
        public static final String LOGIN = "/views/login.jsp";
        public static final String REGISTER = "/views/register.jsp";
        public static final String HOME = "/views/home.jsp";
        public static final String PROFILE = "/views/profile.jsp";
        public static final String ACTIVATE = "/views/activate.jsp";
        public static final String FORGOT_PASSWORD = "/views/forgot-password.jsp";
        public static final String RESET_PASSWORD = "/views/reset-password.jsp";
    }

    public static final String DIR = System.getProperty("user.home") + java.io.File.separator + "jpa-login-demo-uploads";
}
