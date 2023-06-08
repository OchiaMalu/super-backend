package net.zjitc.mapper;

import net.zjitc.model.domain.BlogComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author OchiaMalu
* @description 针对表【blog_comments】的数据库操作Mapper
* @createDate 2023-06-08 12:44:45
* @Entity net.zjitc.model.domain.BlogComments
*/
@Mapper
public interface BlogCommentsMapper extends BaseMapper<BlogComments> {

}




