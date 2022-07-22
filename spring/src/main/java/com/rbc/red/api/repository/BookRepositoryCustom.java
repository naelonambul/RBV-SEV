package com.rbc.red.api.repository;

import com.rbc.red.api.dto.BookDto;
import com.rbc.red.api.dto.BookSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<BookDto> searchPageComplex(Long teamId, BookSearchCondition condition, Pageable pageable);
}
