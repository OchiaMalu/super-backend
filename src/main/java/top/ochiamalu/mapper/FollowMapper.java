package top.ochiamalu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ochiamalu.model.domain.Follow;

/**
* @author OchiaMalu
* @description 针对表【follow】的数据库操作Mapper
* @createDate 2023-06-11 13:02:31
 * @Entity top.ochiamalu.model.domain.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




