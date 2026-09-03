package vn.iotstar.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.User;

public class UserDaoImpl implements IUserDao {

    @Override
    public void insert(User user) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user); // insert vào bảng Users
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.userName = :username";
            TypedQuery<User> query = enma.createQuery(jpql, User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Không tìm thấy tài khoản -> trả về null thay vì ném lỗi
            return null;
        } finally {
            enma.close();
        }
    }

    @Override
    public User findById(int id) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.find(User.class, id);
        } finally {
            enma.close();
        }
    }

    /**
     * Cập nhật (update) dùng cho chức năng "Hồ sơ cá nhân": fullname, phone, avatar.
     * User truyền vào là một entity đã bị detach (được findById() ở tầng service
     * rồi set lại field), nên dùng merge() để JPA sinh câu UPDATE tương ứng.
     */
    @Override
    public void update(User user) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
            Long count = enma.createQuery(jpql, Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistUsername(String username) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM User u WHERE u.userName = :username";
            Long count = enma.createQuery(jpql, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistPhone(String phone) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM User u WHERE u.phone = :phone";
            Long count = enma.createQuery(jpql, Long.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            enma.close();
        }
    }
}
