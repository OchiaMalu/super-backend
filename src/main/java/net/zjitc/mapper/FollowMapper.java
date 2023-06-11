package net.zjitc.mapper;

import net.zjitc.model.domain.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author OchiaMalu
* @description 针对表【follow】的数据库操作Mapper
* @createDate 2023-06-11 13:02:31
* @Entity net.zjitc.model.domain.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




