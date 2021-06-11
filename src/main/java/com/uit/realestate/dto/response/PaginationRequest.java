package com.uit.realestate.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class PaginationRequest {

    /**
     * Page number.
     */
    private Integer page = 1;

    /**
     * Page size.
     */
    private Integer size = 10;

    private Pageable pageable;

    /**
     * Constructor.
     *
     * @param page page number
     * @param size page size
     */
    public PaginationRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    /**
     * Create pageable
     *
     * @param sortType sort type
     * @param sortBy   sort by
     * @return Pageable
     */
    public void createPageable(final Sort.Direction sortType, final String sortBy) {
        this.pageable = PageRequest.of(page - 1, size, Sort.by(sortType, sortBy));
    }

    /**
     * Create pageable
     *
     * @return Pageable
     */
    public void createPageable() {
        this.pageable = PageRequest.of(page - 1, size);
    }

    /**
     * Create pageable
     *
     * @param sort
     */
    public void createPageable(final Sort sort) {
        this.pageable = PageRequest.of(page - 1, size, sort);
    }
}

