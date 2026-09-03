package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Lưu ý: EntityManagerFactory rất "nặng" để khởi tạo, nên chỉ nên tạo
 * MỘT LẦN DUY NHẤT cho cả ứng dụng (static), sau đó mỗi request/luồng
 * xử lý sẽ mở một EntityManager riêng từ factory này rồi close() sau khi dùng.
 */
public class JpaConfig {

    private static final String PERSISTENCE_UNIT_NAME = "jpa-hibernate-mysql";
    private static EntityManagerFactory factory;

    static {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
