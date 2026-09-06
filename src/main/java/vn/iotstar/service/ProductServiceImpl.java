package vn.iotstar.service;

import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.ProductDaoImpl;
import vn.iotstar.entity.Product;

public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao = new ProductDaoImpl();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public void delete(int id) throws Exception {
        productDao.delete(id);
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findLatest(int limit) {
        return productDao.findLatest(limit);
    }

    @Override
    public List<Product> findPage(int page, int pageSize) {
        return productDao.findPage(page, pageSize);
    }

    @Override
    public long countAll() {
        return productDao.countAll();
    }

    @Override
    public int totalPages(int pageSize) {
        long total = countAll();
        return (int) Math.ceil((double) total / pageSize);
    }

    @Override
    public List<Product> searchByName(String keyword) {
        return productDao.searchByName(keyword);
    }
}
