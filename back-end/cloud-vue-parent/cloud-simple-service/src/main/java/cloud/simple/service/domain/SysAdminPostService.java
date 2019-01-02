package cloud.simple.service.domain;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.simple.service.base.BaseServiceImpl;
import cloud.simple.service.model.SysAdminPost;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class SysAdminPostService extends BaseServiceImpl<SysAdminPost>{
	
	@Autowired
	private Mapper<SysAdminPost> sysAdminPostDao;
	
	public PageInfo<SysAdminPost> getDataList(SysAdminPost post) {

		PageHelper.startPage(post.getPage(),post.getRows());
		Example example = new Example(SysAdminPost.class,false);
		Criteria criteria = example.createCriteria();
		if(StringUtils.isNotBlank(post.getName())){
			criteria.andLike("name", post.getName());
		}
		example.orderBy("id").desc();
		return new PageInfo<>(sysAdminPostDao.selectByExample(example));
	}

}
