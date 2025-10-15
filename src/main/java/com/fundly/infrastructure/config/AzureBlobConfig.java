package com.fundly.infrastructure.config;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobConfig {
    @Bean
    public BlobServiceClient blobServiceClient() {
        DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();

        return new BlobServiceClientBuilder()
                .endpoint("https://fundlyblob.blob.core.windows.net/")
                .credential(defaultCredential)
                .buildClient();
    }
}
