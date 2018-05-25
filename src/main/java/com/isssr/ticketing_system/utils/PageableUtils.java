package com.isssr.ticketing_system.utils;

import com.isssr.ticketing_system.exception.PageableQueryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Page utils class
 * Use it to get Pageable instances to perform pageable queries
 **/
@Component
public class PageableUtils {

    @Value("${pageable.queries.default.size}")
    private int defaultPageSize;

    @Value("${pageable.queries.doubling.size}")
    private boolean doublingPageSize;

    @Value("${pageable.queries.doubling.size.threshold}")
    private int doublingThreshold;

    /**
     * Return a page request
     **/
    public PageRequest instantiatePageableObject(@NotNull Integer page,
                                                 @Nullable Integer pageSize,
                                                 @Nullable Sort sort) throws PageableQueryException {
        if (page == null || page < 0)
            throw new PageableQueryException("Page number not defined or negative");

        if (sort == null)
            return instantiateSimplePageableObject(page, pageSize);

        return instantiateSortedPageableObject(page, pageSize, sort);
    }

    /**
     * return a page request with sorting
     **/
    private PageRequest instantiateSortedPageableObject(@NotNull Integer page,
                                                        Integer pageSize,
                                                        Sort sort) {

        if (pageSize != null || pageSize > 0)
            return PageRequest.of(page, pageSize, sort);

        if (doublingPageSize) {
            if (page <= doublingThreshold)
                return PageRequest.of(page, defaultPageSize * (page + 1), sort);
            else
                return PageRequest.of(page, defaultPageSize * doublingThreshold, sort);
        }

        return PageRequest.of(page, defaultPageSize, sort);
    }

    /**
     * return a page request without sorting
     **/
    private PageRequest instantiateSimplePageableObject(@NotNull Integer page,
                                                        Integer pageSize) {

        if (pageSize != null && pageSize > 0)
            return PageRequest.of(page, pageSize);

        if (doublingPageSize) {
            if (page <= doublingThreshold)
                return PageRequest.of(page, defaultPageSize * (page + 1));
            else
                return PageRequest.of(page, defaultPageSize * doublingThreshold);
        }

        return PageRequest.of(page, defaultPageSize);
    }

    /**
     * generate a sort object with different sorting directions and different properties
     * DEFAULT order direction ASC
     **/
    public Sort instantiateSortObject(@Nullable SortingDirection[] sortingDirections,
                                      @NotNull String... properties) throws PageableQueryException {

        //check property
        if (!checkPropertiesExistence(properties))
            throw new PageableQueryException("No sorting property");

        if (sortingDirections.length > properties.length)
            throw new PageableQueryException("To much sorting directions");

        Sort sort = instantiateSimpleSortObject(sortingDirections[0], properties[0]);

        for (int i = 1; i < properties.length; i++) {

            sort.and(instantiateSimpleSortObject(sortingDirections[i], properties[i]));

        }

        return sort;

    }

    /**
     * generate a simple sort object with sorting order direction and single property
     * DEFAULT order direction ASC
     */
    private Sort instantiateSimpleSortObject(SortingDirection sortingDirection,
                                             String property) throws PageableQueryException {

        //check property
        if (!checkPropertiesExistence(property))
            throw new PageableQueryException("No sorting property");

        switch (sortingDirection) {
            case ASC:
                return Sort.by(Sort.Order.asc(property));
            case DESC:
                return Sort.by(Sort.Order.desc(property));
            default:
                return Sort.by(Sort.Order.asc(property));
        }

    }

    /**
     * check that properties array is not null,
     * with elements in there and that every single element is not equals to ""
     */
    private boolean checkPropertiesExistence(String... properties) {

        if (properties == null || properties.length == 0)
            return false;

        for (int i = 0; i < properties.length; i++) {
            if (properties[i].equals(""))
                return false;
        }

        return true;
    }

    public enum SortingDirection {ASC, DESC}

}
