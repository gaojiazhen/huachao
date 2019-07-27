package com.fjhcit.common.kit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import io.minio.MinioClient;

/**
 * @Description：文件上传工具类
 * @author：陈 麟
 * @date：2019年06月04日 下午21:37:31
 */
@SuppressWarnings("rawtypes")
public class MinioUtil {

	private static String minio_url = "http://127.0.0.1:9000";

	//private static String minio_name = "NQFAXBUGFYUY2UM5O0EJ";
	private static String minio_name = "9LVFRN8778I64ZL1I6ZT";
	//private static String minio_pass = "66CKW0VjFXBLV23DxSJ3gc07vZZJtUuCxuUXz2sU";
	private static String minio_pass = "3KFFXiZJlqh3X6M6jdvJN1V1PHqLZnVIEYFLBLvx";

	private static String minio_bucketName = "retire";
	public final static Map<String, Integer> FILE_TYPE_MAP = new HashMap<String, Integer>();

	static {
		FILE_TYPE_MAP.put("bmp", 1);
		FILE_TYPE_MAP.put("jpg", 1);
		FILE_TYPE_MAP.put("jpeg", 1);
		FILE_TYPE_MAP.put("png", 1);
		FILE_TYPE_MAP.put("tiff", 1);
		FILE_TYPE_MAP.put("gif", 1);
		FILE_TYPE_MAP.put("pcx", 1);
		FILE_TYPE_MAP.put("tga", 1);
		FILE_TYPE_MAP.put("exif", 1);
		FILE_TYPE_MAP.put("fpx", 1);
		FILE_TYPE_MAP.put("svg", 1);
		FILE_TYPE_MAP.put("psd", 1);
		FILE_TYPE_MAP.put("cdr", 1);
		FILE_TYPE_MAP.put("pcd", 1);
		FILE_TYPE_MAP.put("dxf", 1);
		FILE_TYPE_MAP.put("ufo", 1);
		FILE_TYPE_MAP.put("eps", 1);
		FILE_TYPE_MAP.put("ai", 1);
		FILE_TYPE_MAP.put("raw", 1);
		FILE_TYPE_MAP.put("wmf", 1);
		FILE_TYPE_MAP.put("txt", 2);
		FILE_TYPE_MAP.put("doc", 2);
		FILE_TYPE_MAP.put("docx", 2);
		FILE_TYPE_MAP.put("xls", 2);
		FILE_TYPE_MAP.put("htm", 2);
		FILE_TYPE_MAP.put("html", 2);
		FILE_TYPE_MAP.put("jsp", 2);
		FILE_TYPE_MAP.put("rtf", 2);
		FILE_TYPE_MAP.put("wpd", 2);
		FILE_TYPE_MAP.put("pdf", 2);
		FILE_TYPE_MAP.put("ppt", 2);
		FILE_TYPE_MAP.put("mp4", 3);
		FILE_TYPE_MAP.put("avi", 3);
		FILE_TYPE_MAP.put("mov", 3);
		FILE_TYPE_MAP.put("wmv", 3);
		FILE_TYPE_MAP.put("asf", 3);
		FILE_TYPE_MAP.put("navi", 3);
		FILE_TYPE_MAP.put("3gp", 3);
		FILE_TYPE_MAP.put("mkv", 3);
		FILE_TYPE_MAP.put("f4v", 3);
		FILE_TYPE_MAP.put("rmvb", 3);
		FILE_TYPE_MAP.put("webm", 3);
		FILE_TYPE_MAP.put("mp3", 4);
		FILE_TYPE_MAP.put("wma", 4);
		FILE_TYPE_MAP.put("wav", 4);
		FILE_TYPE_MAP.put("mod", 4);
		FILE_TYPE_MAP.put("ra", 4);
		FILE_TYPE_MAP.put("cd", 4);
		FILE_TYPE_MAP.put("md", 4);
		FILE_TYPE_MAP.put("asf", 4);
		FILE_TYPE_MAP.put("aac", 4);
		FILE_TYPE_MAP.put("vqf", 4);
		FILE_TYPE_MAP.put("ape", 4);
		FILE_TYPE_MAP.put("mid", 4);
		FILE_TYPE_MAP.put("ogg", 4);
		FILE_TYPE_MAP.put("m4a", 4);
		FILE_TYPE_MAP.put("vqf", 4);
	}

	/**
	 * @Title: uploadImage
	 * @Description:上传图片
	 * @param inputStream
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public static Map uploadImage(InputStream inputStream, String suffix) throws Exception {
		return upload(inputStream, suffix, "image/jpeg");
	}

	/**
	 * @Title: uploadVideo
	 * @Description:上传视频
	 * @param inputStream
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public static Map uploadVideo(InputStream inputStream, String suffix) throws Exception {
		return upload(inputStream, suffix, "video/mp4");
	}

	/**
	 * @Title: uploadVideo
	 * @Description:上传文件
	 * @param inputStream
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public static Map uploadFile(InputStream inputStream, String suffix) throws Exception {
		return upload(inputStream, suffix, "application/octet-stream");
	}

	/**
	 * 上传字符串大文本内容
	 * @Title: uploadString
	 * @Description:描述方法
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Map uploadString(String str) throws Exception {
		if (!StringUtils.notNullAndEmpty(str)) {
			return new HashMap();
		}
		InputStream inputStream = new ByteArrayInputStream(str.getBytes());
		return upload(inputStream, null, "text/html");
	}

	public static Map<String, String> uploadfile(InputStream inputStream, String fileName, String contentType) throws Exception {
		String key = "." + getFileType(fileName).toLowerCase();
		Map<String, String> map = upload(inputStream, key, contentType);
		map.put("fileName", fileName);
		return map;
	}

	/**
	 * @Title: upload
	 * @Description:上传主功能
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> upload(InputStream inputStream, String suffix, String contentType) throws Exception {
		Map<String, String> map = new HashMap<>();
		MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
		// minioClient.get
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String ymd = sdf.format(new Date());
		String objectName = ymd + "/" + UUID.randomUUID().toString() + (suffix != null ? suffix : "");
		minioClient.putObject(minio_bucketName, objectName, inputStream, contentType);
		//String url = minioClient.getObjectUrl(minio_bucketName, objectName);
		map.put("flag", "0");
		map.put("mess", "上传成功");
		map.put("objectName", objectName);
		map.put("contentType", contentType);
		// map.put("urlval", url);
		return map;
	}

	public static ResponseEntity<InputStreamResource> downFile(Map<String, String> param) throws Exception {
		ResponseEntity<InputStreamResource> response = null;
		MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
		InputStream in = minioClient.getObject(minio_bucketName, param.get("objectName"));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("content-type", param.get("contentType"));
		String filename = new String(param.get("fileName").getBytes("GBK"), "ISO-8859-1");
		headers.add("Content-Disposition", "attachment; filename=" + filename);
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		response = ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		return response;
	}

	public static String getFileType(String fileName) {
		String[] strArray = fileName.split("\\.");
		int suffixIndex = strArray.length - 1;
		System.out.println(strArray[suffixIndex]);
		return strArray[suffixIndex];
	}
}