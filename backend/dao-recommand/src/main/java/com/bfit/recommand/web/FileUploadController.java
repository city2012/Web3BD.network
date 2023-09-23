package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController("common")
@RequestMapping("/common")
public class FileUploadController {

    private static final String PATH = "common/media/"; // 替换为您的文件保存目录
    private static final String FILE_PREFIX = "0x"; // 替换为您的文件保存目录

    @PostMapping("/upload")
    public CommonResult<String> uploadFile(@RequestParam("file") MultipartFile file,
                                           HttpServletRequest request) {
        if (file.isEmpty()) {
            return CommonResult.<String>builder().code(400).errMsg("Please select a file to upload.").build();
        }
        File destFile = null;
        String fileName = "";
        try {
            String realPath = request.getSession().getServletContext().getRealPath(PATH);
            File dir = new File(realPath);
            if (!dir.isDirectory()){
                dir.mkdirs();
            }
            // 定义文件保存路径
            String fileNameMid = StringUtils.replace(UUID.randomUUID().toString(), "-", "");
            String originalFilename = file.getOriginalFilename();
            fileName = FILE_PREFIX + fileNameMid + originalFilename.substring(originalFilename.lastIndexOf("."));
            destFile = new File(dir, fileName);

            // 将文件保存到指定目录
            file.transferTo(destFile);
            return CommonResult.ok(request.getScheme() + "://" +
                    request.getServerName() + ":"
                    + request.getServerPort()
                    + PATH + fileName);
        } catch (IOException e) {
            log.error("uploadFile error :: ", e);
        }
        return CommonResult.<String>builder().code(500).errMsg("Server side error, please wait kindly.").build();
    }
}

