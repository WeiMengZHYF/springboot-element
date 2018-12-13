package cloud.simple.service.domain;

import cloud.simple.service.model.SysAdminStructure;
import cloud.simple.service.util.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cloud.simple.service.base.BaseServiceImpl;
import cloud.simple.service.contants.Constant;
import cloud.simple.service.dao.SysAdminUserDao;
import cloud.simple.service.model.SysAdminUser;
import cloud.simple.service.util.EncryptUtil;
import cloud.simple.service.util.FastJsonUtils;

import java.util.Optional;

@Service
public class SysAdminUserService extends BaseServiceImpl<SysAdminUser> {

    @Autowired
    private SysAdminUserDao sysAdminUserDao;

    @Autowired
    private SysAdminStructureService sysAdminStructureService;

    /**
     * 修改密码
     *
     * @param currentUser 当前登录的用户信息
     * @param old_pwd
     * @param new_pwd
     * @return 修改失败返回错误信息，修改成功返回authKey信息。
     */
    public String setInfo(SysAdminUser currentUser, String old_pwd, String new_pwd) {

        if (currentUser == null) {
            throw new ServiceException(-400, "请先登录");
        }

        if (StringUtils.isNotBlank(old_pwd)) {
            throw new ServiceException(-400, "旧密码必填");
        }

        if (StringUtils.isNotBlank(new_pwd)) {
            throw new ServiceException(-400, "新密码必填");
        }

        if (old_pwd.equals(new_pwd)) {
            throw new ServiceException(-400, "新旧密码不能一样");
        }

        if (!currentUser.getPassword().equals(DigestUtils.md5Hex(old_pwd))) {
            throw new ServiceException(-400, "原密码错误");
        }

        if (!currentUser.getPassword().equals(DigestUtils.md5Hex(old_pwd))) {
            throw new ServiceException(-400, "原密码错误");
        }
        SysAdminUser record = new SysAdminUser();
        record.setId(currentUser.getId());
        String md5NewPwd = DigestUtils.md5Hex(new_pwd);
        record.setPassword(md5NewPwd);
        sysAdminUserDao.updateByPrimaryKeySelective(record);
        return EncryptUtil.encryptBase64(currentUser.getUsername() + "|" + md5NewPwd, Constant.SECRET_KEY);
    }

    public PageInfo<SysAdminUser> getDataList(SysAdminUser record) {
        PageInfo<SysAdminUser> pageInfo = this.selectPage(record.getPage(), record.getRows(), record);
        if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(entity -> {
                if (entity.getStructureId() != null) {
                    SysAdminStructure structure = sysAdminStructureService.selectByPrimaryKey(entity.getStructureId());
                    String name = Optional.ofNullable(structure).map(SysAdminStructure::getName).orElse(null);
                    entity.setStructureName(name);
                }
            });
        }
        return pageInfo;
    }

}
