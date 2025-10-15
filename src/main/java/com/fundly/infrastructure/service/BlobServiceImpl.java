package com.fundly.infrastructure.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.fundly.common.dto.UploadDTO;
import com.fundly.common.port.in.BlobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@Log4j2
public class BlobServiceImpl implements BlobService {
    private final BlobServiceClient blobServiceClient;

    public BlobServiceImpl(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public String upload(UploadDTO uploadDto) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(uploadDto.getContainerName());
        if (!containerClient.exists()) {
            containerClient.create();

            log.info("Successfully created blob container {}.", uploadDto.getContainerName());
        }

        BlobClient blobClient = containerClient.getBlobClient(uploadDto.getBlobName());

        try (ByteArrayInputStream fileStream = new ByteArrayInputStream(uploadDto.getFileBytes())) {
            blobClient.upload(
                    fileStream,
                    uploadDto.getFileBytes().length,
                    true
            );

            return blobClient.getBlobUrl();
        } catch (IOException ex) {
            log.warn("Failed to upload file {} to blob storage. With exception: {}", uploadDto.getBlobName(), ex.getMessage());

            throw new RuntimeException("Failed to upload file " + uploadDto.getBlobName() + " to blob storage.");
        }
    }
}
