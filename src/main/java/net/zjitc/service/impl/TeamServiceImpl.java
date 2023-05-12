package net.zjitc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.mapper.TeamMapper;
import net.zjitc.model.domain.Team;
import net.zjitc.service.TeamService;
import org.springframework.stereotype.Service;

/**
* @author OchiaMalu
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2023-05-12 19:33:37
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

}




