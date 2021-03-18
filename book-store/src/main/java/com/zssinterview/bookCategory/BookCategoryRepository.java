package com.zssinterview.bookCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

	BookCategory findByCategoryTitle(String string);

}
