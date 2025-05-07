package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.swiftboot.web.response.Response;

/**
 * @author swiftech
 **/
@Tag(name = "UploadFile上传文件")
@Controller
@RequestMapping("/file")
public class UploadFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);


    @Operation(description = "单个文件上传")
    @RequestMapping(value = "upload/single", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public
    @ResponseBody
    Response<Void> fileUpload(
            @Parameter @RequestPart(value = "file") MultipartFile file,
            HttpServletResponse response,
            HttpServletRequest request) {
        log.info("> /file/upload/single");
        log.info("上传文件：" + file.getName());
        log.info("  " + file.getOriginalFilename());
        return new Response<>();
    }
}
