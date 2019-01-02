package cloud.simple.service.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cloud.simple.service.base.BaseServiceImpl;
import cloud.simple.service.dao.SysAdminGroupDao;
import cloud.simple.service.dao.SysAdminMenuDao;
import cloud.simple.service.model.SysAdminGroup;
import cloud.simple.service.model.SysAdminMenu;
import cloud.simple.service.util.BeanToMapUtil;
import cloud.simple.service.util.Category;

@Service
public class SysAdminMenuService extends BaseServiceImpl<SysAdminMenu> {

    @Autowired
    private SysAdminMenuDao sysAdminMenuDao;

    @Autowired
    private SysAdminGroupDao sysAdminGroupDao;

    /**
     * 获取用户对应的菜单
     *
     * @param userId
     * @return
     */
    public List<SysAdminMenu> getTreeMenuByUserId(Integer userId) {

        //查看用户对应未禁用的菜单
        List<SysAdminMenu> menusList = getMenusByUserId(userId, (byte) 1);
        return this.buildByRecursiveTree(menusList);
    }

    /**
     * 根据用户id查询所属的菜单信息
     *
     * @param userId 用户id
     * @param status 状态 0：禁用，1：启用，null：全部
     * @return
     */
    private List<SysAdminMenu> getMenusByUserId(Integer userId, Byte status) {
        List<SysAdminMenu> menusList;
        //判断是否为管理员
        if (userId.equals(1)) {
            SysAdminMenu menu = new SysAdminMenu();
            menu.setStatus(status);
            menusList = this.select(menu);
        } else {
            //查询分组
            List<SysAdminGroup> groupsList = sysAdminGroupDao.selectByUserId(userId, status);
            StringBuilder ruleIds = new StringBuilder();
            for (SysAdminGroup group : groupsList) {
                if (ruleIds.length() == 0) {
                    ruleIds.append(group.getRules());
                } else {
                    ruleIds.append(",").append(group.getRules());
                }
            }
            //查询菜单
            menusList = sysAdminMenuDao.selectInRuleIds(ruleIds.toString(), 1);
        }
        return menusList;
    }

    /**
     * 使用递归方法建树
     *
     * @param rootSysAdminMenus 原始的数据
     * @return
     */
    private List<SysAdminMenu> buildByRecursiveTree(List<SysAdminMenu> rootSysAdminMenus) {

        List<SysAdminMenu> trees = new ArrayList<>();
        for (SysAdminMenu menu : rootSysAdminMenus) {
            if ("0".equals(menu.getPid().toString())) {
                trees.add(getChild(menu, rootSysAdminMenus, 1));
            }
        }
        return trees;
    }

    /**
     * 递归查找子菜单
     *
     * @param treeMenu  当前菜单id
     * @param treeNodes 要查找的列表
     * @param level     级别
     * @return
     */
    private SysAdminMenu getChild(SysAdminMenu treeMenu, List<SysAdminMenu> treeNodes, int level) {

        treeMenu.setSelected(false);
        treeMenu.setLevel(level);
        for (SysAdminMenu it : treeNodes) {
            if (treeMenu.getId().equals(it.getPid())) {
                if (treeMenu.getChild() == null) {
                    treeMenu.setChild(new ArrayList<>());
                }
                treeMenu.getChild().add(getChild(it, treeNodes, level + 1));
            }
        }
        return treeMenu;
    }


    /**
     * 查询对应用户Id的菜单
     *
     * @return
     */
    public PageInfo<SysAdminMenu> getDataList(SysAdminMenu record) {

        PageInfo<SysAdminMenu> pageInfo = this.selectPage(record.getPage(), record.getRows(), record);
        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
            pageInfo.getList().forEach(entity->{
                String parent = Optional.ofNullable(entity.getPid()).map(this::selectByPrimaryKey).map(SysAdminMenu::getTitle).orElse(null);
                entity.setParent(parent);
            });
        }
        return pageInfo;
    }
}
