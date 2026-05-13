package com.ashay.assignment1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MovieResponse {
    private int page;
    private List<Object> data;

    @JsonProperty("per_page")
    private int perPage;
    private int total;

    @JsonProperty("total_pages")
    private  int totalPages;

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Object> getData() {
        return data;
    }
}
