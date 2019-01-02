package cloud.simple.service.domain;

import java.util.Optional;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import cloud.simple.service.base.BaseServiceImpl;
import cloud.simple.service.model.SysAdminStructure;

@Service
public class SysAdminStructureService extends BaseServiceImpl<SysAdminStructure>{

	public PageInfo<SysAdminStructure> getDataList(SysAdminStructure sysAdminStructure) {

		PageInfo<SysAdminStructure> pageInfo = this.selectPage(sysAdminStructure.getPage(), sysAdminStructure.getRows(), sysAdminStructure);
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(entity ->{
				String parent = Optional.ofNullable(entity.getPid()).map(this::selectByPrimaryKey).map(SysAdminStructure::getName).orElse(null);
				entity.setParent(parent);
			});
		}
		return pageInfo;
	}

}
