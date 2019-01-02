package cloud.simple.service.web;

import javax.servlet.http.HttpServletResponse;

import cloud.simple.service.util.RestResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cloud.simple.service.util.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件上传控制器
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:11:42
 */
@RestController
@Api(value = "文件上传接口")
@RequestMapping(value = "/upload")
public class UploadController extends CommonController {
	
	@Value("${spring.servlet.multipart.location}")
	private String multipartLocation;
	
	// 上传文件(支持批量)
	@PostMapping("/image")
	@ApiOperation(value = "上传图片", httpMethod="POST")
	public RestResult<String> uploadImage(MultipartFile file, HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		 //上传文件
        String path = UploadUtils.saveMartipartFile(multipartLocation,file,"users","yyyyMM");
        return RestResult.success(path);
	}
}
