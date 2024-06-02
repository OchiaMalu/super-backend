package top.ochiamalu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ochiamalu.model.domain.Team;

/**
* @author OchiaMalu
* @description 针对表【team(队伍)】的数据库操作Mapper
* @createDate 2023-05-12 19:33:37
* @Entity generator.domain.Team
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

}




