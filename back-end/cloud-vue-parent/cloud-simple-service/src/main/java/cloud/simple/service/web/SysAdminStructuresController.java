package cloud.simple.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import cloud.simple.service.util.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.domain.SysAdminStructureService;
import cloud.simple.service.model.SysAdminStructure;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统权限控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/structures")
@Api(value = "SysAdminStructuresController")
public class SysAdminStructuresController extends CommonController {

    @Autowired
    private SysAdminStructureService sysAdminStructureService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "GET")
    @RequestMapping(value = "")
    public RestResult<List<Map<String, Object>>> index() {

        return RestResult.success(sysAdminStructureService.getDataList());
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}")
    public RestResult<SysAdminStructure> read(@PathVariable("id") Integer id) {

        return RestResult.success(sysAdminStructureService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<Object> save(@RequestBody SysAdminStructure record) {

        sysAdminStructureService.save(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新")
    @PostMapping(value = "update")
    public RestResult<Object> update(@RequestBody SysAdminStructure record) {

        sysAdminStructureService.updateByPrimaryKeySelective(record);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}")
    public RestResult<Object> delete(@PathVariable Integer id) {

        sysAdminStructureService.deleteByPrimaryKey(id);
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
        this.sysAdminStructureService.deleteByPrimaryKeys(Arrays.asList(ids.split(",")));
        return RestResult.success();
    }
}
