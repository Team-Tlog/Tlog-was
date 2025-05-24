package com.se.Tlog.global.util.Image;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ImageUploadUtil {
    public static String uploadWebToFirebase(MultipartFile imageFile, String path) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        BufferedImage webpImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        webpImage.getGraphics().drawImage(originalImage, 0, 0, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(webpImage, "webp", outputStream);
        outputStream.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // firebase 업로드
        Bucket bucket = StorageClient.getInstance().bucket();


        Blob blob = bucket.create(
                path,
                inputStream,
                "image/webp"
        );

        // 퍼블릭 읽기 권한 부여
        blob.toBuilder()
                .setAcl(List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build()
                .update();

        // 퍼블릭 CDN URL 생성
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" +
                bucket.getName() + "/o/" +
                URLEncoder.encode(path, StandardCharsets.UTF_8) + "?alt=media";
        return imageUrl;
    }
}
