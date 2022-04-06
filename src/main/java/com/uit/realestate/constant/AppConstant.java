package com.uit.realestate.constant;

/**
 * AppConstant
 * Description: Using to create constant variable.
 */
public interface AppConstant {

    /**
     * Index of current page default.
     */
    String PAGE_NUMBER_DEFAULT = "1";
    String PAGE_SIZE_DEFAULT = "6";


    /**
     * Size element of page default.
     */
    String FILE_SIZE_LIMIT = "file.attach.size";

    /**
     * Default rating
     */
    Long DEFAULT_RATING = 1L;

    /**
     * Default favourite rating
     */
    Long DEFAULT_FAVOURITE_RATING = 5L;

    /**
     * Default disable favourite rating
     */
    Long DEFAULT_DISABLE_FAVOURITE_RATING = -5L;


    Long DEFAULT_MAX_VALUE_PRICE = 10000000000L;

    Long DEFAULT_MAX_VALUE_AREA = 1000L;

    Long ADMIN_ID_ACCOUNT = -1L;

}
