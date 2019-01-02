package cloud.simple.service.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传工具类
 *
 * @version 1.0
 */
public class UploadUtils {

    public static final String JPG_JPGE_BMP_GIF_PNG = "jpg,jpge,bmp,gif,png";

    public static String saveMartipartFile(String multipartLocation, MultipartFile file) {
        return saveMartipartFile(multipartLocation, file, null, "yyyyMMdd");
    }

    public static String saveMartipartFile(String multipartLocation, MultipartFile file, String module) {
        return saveMartipartFile(multipartLocation, file, module, "yyyyMMdd");
    }

    public static String saveMartipartFile(String multipartLocation, MultipartFile file, String module, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String dateString = simpleDateFormat.format(new Date());
            File dir = new File(multipartLocation + "/upload/" + (StringUtils.isNotEmpty(module) ? module + "/" + dateString : dateString));
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new Exception("创建保存目录失败");
                }
            }
            String fileName = UUID.randomUUID().toString() + "."
                    + FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
            file.transferTo(new File(dir, fileName));
            return "/upload/" + (StringUtils.isNotEmpty(module) ? module + "/" + dateString : dateString) + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
