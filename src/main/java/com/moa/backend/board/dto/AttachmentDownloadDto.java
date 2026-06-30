package com.moa.backend.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AttachmentDownloadDto {
    private String originalName;
    private byte[] fileData;
}