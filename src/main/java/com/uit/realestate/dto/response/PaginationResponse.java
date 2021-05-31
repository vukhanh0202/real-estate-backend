package com.uit.realestate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.AppConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginationResponse<T> {

    /**
     * Contents of page.
     */
    private List<T> contents;

    /**
     * Total item all of page.
     */
    private Integer totalItem;

    /**
     * Total item of page.
     */
    private Integer amount;

    /**
     * Current page.
     */
    private Integer page;

    /**
     * Constructor.
     * @param total total item all of page
     * @param amount size element of page
     * @param page index of current page
     * @param contents contents of page
     */
    public PaginationResponse(Integer total, Integer amount, Integer page, List<T> contents){
        this.contents =  contents;
        this.totalItem = total;
        this.page = page;
        this.amount = amount;
    }

    /**
     * Get total pages.
     * @return
     */
    @JsonProperty("total_page")
    public Integer totalPage() {
        return amount > 0 ? (totalItem - 1) / amount + 1 : 0;
    }

    /**
     * Check current page is first page or not.
     * @return
     */
    @JsonProperty("is_first")
    public Boolean isFirst() {
        return page == Integer.parseInt(AppConstant.PAGE_NUMBER_DEFAULT);
    }

    /**
     * Check current page is last page or not.
     * @return
     */
    @JsonProperty("is_last")
    public Boolean isLast() {
        return page * amount >= totalItem;
    }

}
