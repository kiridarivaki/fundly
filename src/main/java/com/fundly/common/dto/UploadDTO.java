package com.fundly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadDTO {
    String blobName;

    String containerName;

    String fileType;

    byte[] fileBytes;
}
