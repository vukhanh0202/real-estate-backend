package com.uit.realestate.repository.category;

import com.uit.realestate.domain.apartment.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIsDeletedFalse();

    Page<Category> findAllByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

    Page<Category> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Category> findByNameAndIsDeletedFalse(String name);

    Optional<Category> findByName(String name);
}
