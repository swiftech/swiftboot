package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.swiftboot.web.result.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author swiftech
 **/
@Api("UploadFile上传文件")
@Controller
@RequestMapping("/file")
public class UploadFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);


    @ApiOperation(notes = "单个文件上传", consumes = "multipart/form-data", value = "单个文件上传")
    @RequestMapping(value = "upload/single", method = RequestMethod.POST, consumes = "multipart/form-data")
    public
    @ResponseBody
    HttpResponse<Void> fileUpload(
            @ApiParam @RequestPart(value = "file") MultipartFile file,
            HttpServletResponse response,
            HttpServletRequest request) {
        log.info("> /file/upload/single");
        log.info("上传文件：" + file.getName());
        log.info("  " + file.getOriginalFilename());
        return new HttpResponse<>();
    }
}
