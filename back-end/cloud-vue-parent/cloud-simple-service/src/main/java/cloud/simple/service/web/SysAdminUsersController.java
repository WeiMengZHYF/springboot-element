package cloud.simple.service.web;

import java.util.Arrays;
import java.util.Map;
import cloud.simple.service.util.RestResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import cloud.simple.service.domain.SysAdminUserService;
import cloud.simple.service.model.SysAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统用户控制层
 *
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/users")
@Api(value = "SysAdminUsersController")
public class SysAdminUsersController extends CommonController {

    @Autowired
    private SysAdminUserService sysAdminUserService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping(value = "")
    public RestResult<PageInfo<SysAdminUser>> index(@RequestBody SysAdminUser record) {

        return RestResult.success(sysAdminUserService.getDataList(record));
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @RequestMapping(value = "edit/{id}")
    public RestResult<SysAdminUser> read(@PathVariable("id") Integer id) {

        return RestResult.success(sysAdminUserService.selectByPrimaryKey(id));
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save")
    public RestResult<Object> save(@RequestBody SysAdminUser record) {

        sysAdminUserService.save(record);
        return RestResult.success();
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新")
    @PostMapping(value = "update")
    public RestResult<Object> update(@RequestBody SysAdminUser record) {

        if(StringUtils.isNotBlank(record.getPassword())){
            record.setPassword(DigestUtils.md5Hex(record.getPassword()));
        }
        sysAdminUserService.updateByPrimaryKeySelective(record);
        return RestResult.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "delete")
    public RestResult<Object> delete(@RequestBody SysAdminUser record) {

        if(record.getId() == null){
            return RestResult.parameter(record,"id不可为空");
        }
        sysAdminUserService.deleteByPrimaryKey(record.getId());
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
        sysAdminUserService.deleteByPrimaryKeys(Arrays.asList(ids.split(",")));
        return RestResult.success();
    }
}
