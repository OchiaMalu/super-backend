package net.zjitc.mapper;

import net.zjitc.model.domain.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author OchiaMalu
* @description 针对表【blog】的数据库操作Mapper
* @createDate 2023-06-03 15:54:34
* @Entity net.zjitc.model.domain.Blog
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}




