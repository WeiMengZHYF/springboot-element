package cloud.simple.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cloud.simple.service.util.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.domain.SysAdminGroupService;
import cloud.simple.service.model.SysAdminGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统分组 控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/groups")
@Api(value = "SysAdminGroupsController", description = "系统分组接口")
public class SysAdminGroupsController extends CommonController {

    @Autowired
    private SysAdminGroupService sysAdminGroupService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "GET")
    @GetMapping(value = "")
    public RestResult<List<Map<String, Object>>> index() {
        return RestResult.success(sysAdminGroupService.getDataList());
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}")
    public RestResult<SysAdminGroup> read(@PathVariable Integer id) {

        return RestResult.success(sysAdminGroupService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<Object> save(@RequestBody SysAdminGroup record) {

        if (record.getPid() == null) {
            record.setPid(0);
        }
        sysAdminGroupService.saveOrUpdate(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新")
    @PostMapping(value = "update")
    public RestResult<String> update(@RequestBody SysAdminGroup record) {

        sysAdminGroupService.updateByPrimaryKeySelective(record);
        return RestResult.success("更新成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    public RestResult<String> delete(@PathVariable Integer id) {

        sysAdminGroupService.deleteByPrimaryKey(id);
        return RestResult.success("删除成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据ids批量删除")
    @PostMapping(value = "deletes")
    public RestResult<Object> deletes(@RequestBody Map<String, Object> params) {

        if (params.get("ids") == null) {
            RestResult.parameter(params, "删除失败");
        }
        String ids = MapUtils.getString(params, "ids");
        String[] list = ids.split(",");
        sysAdminGroupService.deleteByPrimaryKey(Arrays.asList(list));
        return RestResult.success();
    }
}
