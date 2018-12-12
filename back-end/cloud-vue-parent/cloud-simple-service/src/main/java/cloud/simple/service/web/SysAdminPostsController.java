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
import cloud.simple.service.domain.SysAdminPostService;
import cloud.simple.service.model.SysAdminPost;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统岗位控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/posts")
@Api(value = "SysAdminPostsController")
public class SysAdminPostsController extends CommonController {

    @Autowired
    private SysAdminPostService sysAdminPostService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "GET")
    @RequestMapping(value = "")
    public RestResult<List<SysAdminPost>> index(String name) {

        return RestResult.success(sysAdminPostService.getDataList(name));
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}")
    public RestResult<SysAdminPost> read(@PathVariable Integer id) {

        return RestResult.success(sysAdminPostService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<String> save(@RequestBody SysAdminPost record) {

        sysAdminPostService.save(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping(value = "update")
    public RestResult<String> update(@RequestBody SysAdminPost record) {

        sysAdminPostService.updateByPrimaryKeySelective(record);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}")
    public RestResult<String> delete(@PathVariable Integer id) {

        sysAdminPostService.deleteByPrimaryKey(id);
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
        sysAdminPostService.deleteByPrimaryKeys(Arrays.asList(ids.split(",")));
        return RestResult.success();
    }
}
