package com.fjhcit.base.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fjhcit.common.kit.MinioUtil;

/**
 * @description 基础管理_附件上传、下载_控制器
 * @author 陈麟
 * @date 2019年07月11日 上午11:22:20
 */
@RestController
@RequestMapping("/baseFile")
public class BaseFileController {

	/**
	 * @description 上传附件
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			String contentType = file.getContentType();
			InputStream inputStram = file.getInputStream();
			// MinioUtil minioClientUtils = MinioCLinetUtils.getInstance();
			return MinioUtil.uploadfile(inputStram, fileName, contentType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 下载附件
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/downFile", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> downFile(@RequestParam("fileName") String fileName, 
			@RequestParam("objectName") String objectName, @RequestParam("contentType") String contentType) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("fileName", fileName);
			params.put("objectName", objectName);
			params.put("contentType", contentType);
			return MinioUtil.downFile(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}