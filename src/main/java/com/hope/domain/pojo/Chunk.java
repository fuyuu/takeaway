package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {
    private Long id;
    private String docName;
    private String content;
    private Object embedding;
}
