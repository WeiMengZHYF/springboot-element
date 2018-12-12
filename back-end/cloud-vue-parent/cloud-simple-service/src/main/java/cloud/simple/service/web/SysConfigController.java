package cloud.simple.service.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cloud.simple.service.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.domain.SysSystemConfigService;
import cloud.simple.service.model.SysSystemConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统配置 控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin")
@Api(value = "SysConfigController")
public class SysConfigController extends CommonController {

    @Autowired
    private SysSystemConfigService sysSystemConfigService;

    @ApiOperation(value = "获取配置", httpMethod = "POST")
    @PostMapping(value = "/configs")
    public RestResult<Map<String, Object>> configs(@RequestBody SysSystemConfig record) {

        Map<String, Object> data = new HashMap<>();
        List<SysSystemConfig> configs = sysSystemConfigService.select(record);
        configs.forEach(entity ->data.put(entity.getName(),entity.getValue()));
        return RestResult.success(data);
    }
}
