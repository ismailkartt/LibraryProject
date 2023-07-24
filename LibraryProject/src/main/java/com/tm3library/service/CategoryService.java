package com.tm3library.service;

import com.tm3library.domain.Author;
import com.tm3library.domain.Category;
import com.tm3library.dto.request.CategoryRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.CategoryResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ConflictException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.CategoryMapper;
import com.tm3library.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        Category category = categoryMapper.categoryRequestToCategory(categoryRequest);

        if (isCategoryExist(categoryRequest.getName())) {
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_ALREADY_EXIST_EXCEPTION, category.getName()));
        }

        categoryRepository.save(category);

        return categoryMapper.categoryToCategoryResponse(category);
    }

    private boolean isCategoryExist(String categoryName) {

        return categoryRepository.existsByName(categoryName);

    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = findCategoryById(id);

        return categoryMapper.categoryToCategoryResponse(category);
    }

    // Yardımcı method !!!
    public Category findCategoryById(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id)));

        return category;
    }

    public Page<CategoryResponse> findAllWithPage(Pageable pageable) {

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return categoryPage.map(category -> categoryMapper.categoryToCategoryResponse(category));
    }


    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = findCategoryById(id);

        if (category.getBuiltIn()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id));
        }

        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
    }

    public CategoryResponse removeCategoryById(Long id) {

        Category category = findCategoryById(id);
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(category);

        //Built-in
        if (category.getBuiltIn()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION, id));
        }

        // Category kitabı var mı kontrol et !!!
        if (!(category.getBookList().size() == 0)) {
            throw new BadRequestException(String.format(ErrorMessage.CATEGORY_CANNOT_DELETE_EXCEPTION, id));
        }


        categoryRepository.delete(category);

        return categoryResponse;

    }

    public List<Category> getAllCategories() {

        return categoryRepository.getAllBy();
    }
}
