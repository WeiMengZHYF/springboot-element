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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.domain.SysAdminMenuService;
import cloud.simple.service.model.SysAdminMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统菜单控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/menus")
@Api(value = "SysAdminMenusController", description = "系统菜单接口")
public class SysAdminMenusController extends CommonController {

    @Autowired
    private SysAdminMenuService sysAdminMenuService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "POST")
    @RequestMapping(value = "")
    @ResponseBody
    public RestResult<PageInfo<SysAdminMenu>> index(@RequestBody SysAdminMenu record) {

        return RestResult.success(sysAdminMenuService.getDataList(record));
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}")
    public RestResult<SysAdminMenu> read(@PathVariable("id") Integer id) {

        return RestResult.success(sysAdminMenuService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<String> save(@RequestBody SysAdminMenu record) {

        sysAdminMenuService.saveOrUpdate(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping(value = "update")
    public RestResult<String> update(@RequestBody SysAdminMenu record) {

        sysAdminMenuService.updateByPrimaryKeySelective(record);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}")
    public RestResult<String> delete(@PathVariable Integer id) {

        sysAdminMenuService.deleteByPrimaryKey(id);
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
            return RestResult.parameter(params, "操作失败");
        }
        String[] list = ids.split(",");
        sysAdminMenuService.deleteByPrimaryKey(Arrays.asList(list));
        return RestResult.success();
    }
}
