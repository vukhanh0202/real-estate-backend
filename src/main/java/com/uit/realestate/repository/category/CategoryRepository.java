package com.uit.realestate.repository.category;

import com.uit.realestate.domain.apartment.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
