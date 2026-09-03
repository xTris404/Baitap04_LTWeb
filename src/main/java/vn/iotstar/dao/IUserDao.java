package vn.iotstar.dao;

import vn.iotstar.entity.User;

public interface IUserDao {

    void insert(User user);

    User findByUsername(String username);

    User findById(int id);

    void update(User user);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
