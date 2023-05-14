package net.zjitc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.Team;
import net.zjitc.model.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
* @author OchiaMalu
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-05-12 19:33:37
*/
public interface TeamService extends IService<Team> {

    @Transactional(rollbackFor = Exception.class)
    long addTeam(Team team, User loginUser);
}
