package br.com.luangenro.menu.manager.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class ImageService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucket;

  public ImageService(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String saveImage(MultipartFile file, String filename) {

    if (file.isEmpty()) {
      throw new RuntimeException("Uploaded file is empty");
    }

    try {

      PutObjectRequest request =
          PutObjectRequest.builder()
              .bucket(bucket)
              .key("uploads/" + filename)
              .contentType(file.getContentType())
              .build();

      s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

      return generatePublicUrl(filename);

    } catch (S3Exception | IOException e) {
      throw new RuntimeException("Error uploading file to S3", e);
    }
  }

  public void deleteImage(String key) {
    try {
      DeleteObjectRequest deleteRequest =
          DeleteObjectRequest.builder().bucket(bucket).key(key).build();

      s3Client.deleteObject(deleteRequest);

      System.out.println("Deleted image from S3: " + key);

    } catch (S3Exception e) {
      throw new RuntimeException("Failed to delete image from S3: " + key, e);
    }
  }

  private String generatePublicUrl(String filename) {
    return "https://"
        + bucket
        + ".s3."
        + s3Client.serviceClientConfiguration().region().id()
        + ".amazonaws.com/uploads/"
        + filename;
  }
}
