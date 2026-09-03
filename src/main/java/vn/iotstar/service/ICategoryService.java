package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Category;

public interface ICategoryService {

    void insert(Category category);

    void update(Category category);

    void delete(int id) throws Exception;

    Category findById(int id);

    List<Category> findAll();

    List<Category> searchByName(String keyword);
}
