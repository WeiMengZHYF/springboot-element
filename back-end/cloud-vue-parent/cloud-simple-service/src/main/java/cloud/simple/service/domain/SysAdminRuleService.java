package cloud.simple.service.domain;

import java.util.*;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cloud.simple.service.base.BaseServiceImpl;
import cloud.simple.service.dao.SysAdminGroupDao;
import cloud.simple.service.dao.SysAdminRuleDao;
import cloud.simple.service.model.SysAdminGroup;
import cloud.simple.service.model.SysAdminRule;
import cloud.simple.service.util.BeanToMapUtil;
import cloud.simple.service.util.Category;

@Service
public class SysAdminRuleService extends BaseServiceImpl<SysAdminRule> {

    @Autowired
    private SysAdminRuleDao sysAdminRuleDao;

    @Autowired
    private SysAdminGroupDao sysAdminGroupDao;

    /**
     * 根据用户名获取rule数组
     *
     * @param userId 用户id
     */
    public List<SysAdminRule> getTreeRuleByUserId(Integer userId) {
        List<SysAdminRule> rulesList = getRulesByUserId(userId);
        //处理树
        List<SysAdminRule> rulesTreeList = this.buildByRecursiveTree(rulesList);
        return rulesTreeList;
    }


    /**
     * 使用递归方法建树
     *
     * @param rootSysAdminRules 原始的数据
     * @return
     */
    private List<SysAdminRule> buildByRecursiveTree(List<SysAdminRule> rootSysAdminRules) {

        List<SysAdminRule> trees = new ArrayList<>();
        for (SysAdminRule menu : rootSysAdminRules) {
            if ("0".equals(menu.getPid().toString())) {
                trees.add(getChild(menu, rootSysAdminRules, 1));
            }
        }
        return trees;
    }

    /**
     * 递归查找子菜单
     *
     * @param treeMenu  当前菜单id
     * @param treeNodes 要查找的列表
     * @return
     */
    private SysAdminRule getChild(SysAdminRule treeMenu, List<SysAdminRule> treeNodes, int level) {
        for (SysAdminRule it : treeNodes) {
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
     * 给树状规则表处理成 module-controller-action
     *
     * @return treeNodes
     */
    public List<String> rulesDeal(List<SysAdminRule> treeNodes) {
        List<String> ruleStr = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            for (SysAdminRule root : treeNodes) {
                if (CollectionUtils.isNotEmpty(root.getChild())) {
                    for (SysAdminRule c1 : root.getChild()) {
                        if (CollectionUtils.isNotEmpty(c1.getChild())) {
                            for (SysAdminRule c2 : c1.getChild()) {
                                ruleStr.add(root.getName() + "-" + c1.getName() + "-" + c2.getName());
                            }
                        } else {
                            ruleStr.add(root.getName() + "-" + c1.getName());
                        }
                    }
                } else {
                    ruleStr.add(root.getName());
                }

            }
        }
        return ruleStr;
    }

    /**
     * 列表页面
     *
     * @return
     */
    public PageInfo<SysAdminRule> getDataList(SysAdminRule sysAdminRule) {

        PageInfo<SysAdminRule> pageInfo = this.selectPage(sysAdminRule.getPage(), sysAdminRule.getRows(), sysAdminRule);
        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
            pageInfo.getList().forEach(entity ->{
                String parent = Optional.ofNullable(entity.getPid()).map(this::selectByPrimaryKey).map(SysAdminRule::getTitle).orElse(null);
                entity.setParent(parent);
            });
        }
        return pageInfo;
    }


    /**
     * 根据用户id查询所属的权限信息
     *
     * @param userId 用户id
     * @return
     */
    private List<SysAdminRule> getRulesByUserId(Integer userId) {

        List<SysAdminRule> rulesList;
        //判断是否为管理员
        if (userId.equals(1)) {
            rulesList = sysAdminRuleDao.selectByStatus(1);
        } else {
            //查询分组
            List<SysAdminGroup> groupsList = sysAdminGroupDao.selectByUserId(userId, (byte) 1);
            StringBuilder ruleIds = new StringBuilder();
            for (SysAdminGroup group : groupsList) {
                if (ruleIds.length() == 0) {
                    ruleIds.append(group.getRules());
                } else {
                    ruleIds.append(",").append(group.getRules());
                }
            }
            //查询权限
            rulesList = sysAdminRuleDao.selectInIds(ruleIds.toString(), 1);
        }
        return rulesList;
    }
}
