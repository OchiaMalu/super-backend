package top.ochiamalu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ochiamalu.model.domain.BlogLike;

/**
* @author OchiaMalu
* @description 针对表【blog_like】的数据库操作Mapper
* @createDate 2023-06-05 21:54:55
 * @Entity top.ochiamalu.model.domain.BlogLike
*/
@Mapper
public interface BlogLikeMapper extends BaseMapper<BlogLike> {

}




