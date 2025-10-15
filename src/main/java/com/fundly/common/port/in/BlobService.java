package com.fundly.common.port.in;

import com.fundly.common.dto.UploadDTO;

public interface BlobService {
    String upload(UploadDTO uploadDto);
}
