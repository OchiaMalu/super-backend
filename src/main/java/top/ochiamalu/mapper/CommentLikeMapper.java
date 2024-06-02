package top.ochiamalu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ochiamalu.model.domain.CommentLike;

/**
* @author OchiaMalu
* @description 针对表【comment_like】的数据库操作Mapper
* @createDate 2023-06-08 16:24:28
 * @Entity top.ochiamalu.model.domain.CommentLike
*/
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {

}




