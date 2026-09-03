package vn.iotstar.service;

import java.sql.Date;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.UserDaoImpl;
import vn.iotstar.entity.User;

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
    public User updateProfile(int id, String fullName, String phone, String avatar) {
        User user = userDao.findById(id);
        if (user == null) {
            return null;
        }
        user.setFullName(fullName);
        user.setPhone(phone);
        if (avatar != null) {
            // Chỉ ghi đè avatar khi có ảnh mới được upload lên
            user.setAvatar(avatar);
        }
        userDao.update(user);
        return user;
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        // Không cho trùng tài khoản
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date createdDate = new Date(millis);

        // roleid = 3 (Web/thành viên thông thường), avatar mặc định null
        User user = new User(email, username, fullname, password, null, 3, phone, createdDate);
        userDao.insert(user);
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
