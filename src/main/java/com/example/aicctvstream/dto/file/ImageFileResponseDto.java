package com.example.aicctvstream.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageFileResponseDto {
    String fileName;
    String fileUrl;
}
