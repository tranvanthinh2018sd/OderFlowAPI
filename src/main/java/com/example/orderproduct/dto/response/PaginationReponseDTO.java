package com.example.orderproduct.dto.response;

import lombok.*;

@Data
@Builder
public class PaginationReponseDTO {
    private int currentPage;
    private int size;
    private int totalItems;
    private int totalPages;
}
