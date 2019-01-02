package cloud.simple.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import cloud.simple.service.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.domain.SysAdminRuleService;
import cloud.simple.service.model.SysAdminRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统权限控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/rules")
@Api(value = "SysAdminRulesController")
public class SysAdminRulesController extends CommonController {

    @Autowired
    private SysAdminRuleService sysAdminRulesService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping(value = "")
    public RestResult<PageInfo<SysAdminRule>> index(@RequestBody SysAdminRule sysAdminRule) {

        return RestResult.success(sysAdminRulesService.getDataList(sysAdminRule));
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}")
    public RestResult<SysAdminRule> read(@PathVariable("id") Integer id) {

        return RestResult.success(sysAdminRulesService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<String> save(@RequestBody SysAdminRule record) {

        sysAdminRulesService.saveOrUpdate(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping(value = "update")
    public RestResult<String> update(@RequestBody SysAdminRule record) {

        sysAdminRulesService.updateByPrimaryKeySelective(record);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}")
    public RestResult<String> delete(@PathVariable("id") Integer id) {

        sysAdminRulesService.deleteByPrimaryKey(id);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据ids批量删除")
    @PostMapping(value = "deletes")
    public RestResult<Object> deletes(@RequestBody Map<String, String> params) {

        String ids = params.get("ids");
        if (StringUtils.isEmpty(ids)) {
            return RestResult.parameter(params, "删除失败");
        }
        sysAdminRulesService.deleteByPrimaryKeys(Arrays.asList(ids.split(",")));
        return RestResult.success();
    }

}
