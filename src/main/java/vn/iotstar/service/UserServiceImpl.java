package vn.iotstar.service;

import java.sql.Date;
import java.util.Calendar;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.utils.MailUtil;
import vn.iotstar.utils.OtpUtil;

public class UserServiceImpl implements IUserService {

    public IUserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User updateProfile(int id, String fullName, String phone, String avatar) {
        User user = userDao.findById(id);
        if (user == null) {
            return null;
        }
        user.setFullName(fullName);
        user.setPhone(phone);
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        userDao.update(user);
        return user;
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        if (userDao.checkExistEmail(email)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date createdDate = new Date(millis);

        User user = new User(email, username, fullname, password, null, 3, phone, createdDate);
        user.setActive(0);

        String otp = OtpUtil.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(OtpUtil.expiryFromNow());

        userDao.insert(user);
        MailUtil.sendOtp(email, otp, "activate");
        return true;
    }

    @Override
    public boolean activateAccount(String email, String otp) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.isActive()) {
            return true;
        }
        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp.trim())) {
            return false;
        }
        if (OtpUtil.isExpired(user.getOtpExpiry())) {
            return false;
        }
        user.setActive(1);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean resendActivationOtp(String email) {
        User user = userDao.findByEmail(email);
        if (user == null || user.isActive()) {
            return false;
        }
        String otp = OtpUtil.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(OtpUtil.expiryFromNow());
        userDao.update(user);
        MailUtil.sendOtp(email, otp, "activate");
        return true;
    }

    @Override
    public boolean forgotPassword(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }
        String otp = OtpUtil.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(OtpUtil.expiryFromNow());
        userDao.update(user);
        MailUtil.sendOtp(email, otp, "reset");
        return true;
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp.trim())) {
            return false;
        }
        if (OtpUtil.isExpired(user.getOtpExpiry())) {
            return false;
        }
        user.setPassWord(newPassword);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }
}
