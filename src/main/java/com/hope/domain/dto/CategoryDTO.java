package com.hope.domain.dto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long type;
    private List<TagDTO> tags;
}