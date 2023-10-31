package com.clyl.cloudlaw.controller;

import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiugou798
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path}")
    private String IMAGE_PATH;


    @Value("${upload.host}")
    private String HOST;


    @PostMapping("/image")
    public RestBean<Map<String, String>> image(@RequestParam("cover") MultipartFile file) {
        if (file.isEmpty() || !ImageUtil.isImage(file.getOriginalFilename())) {
            return new RestBean<>(501, "上传失败，请选择正确的图片！", null);
        }
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String fileName = uuid + ImageUtil.getFileExtension(file.getOriginalFilename());
            File file1 = new File(IMAGE_PATH + fileName);
            file.transferTo(file1);
            file1.setExecutable(false);
            Map<String, String> map = new HashMap<>();
            map.put("imgUrl", HOST + "/images/" + fileName);
            return new RestBean<>(200, "上传图片成功！", map);
        } catch (IOException e) {
            return new RestBean<>(500, "上传失败，图片异常！", null);
        }
    }

}
