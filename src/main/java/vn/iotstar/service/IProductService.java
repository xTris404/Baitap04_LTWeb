package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Product;

public interface IProductService {

    void insert(Product product);

    void update(Product product);

    void delete(int id) throws Exception;

    Product findById(int id);

    List<Product> findAll();

    List<Product> findLatest(int limit);

    List<Product> findPage(int page, int pageSize);

    long countAll();

    int totalPages(int pageSize);

    List<Product> searchByName(String keyword);
}
