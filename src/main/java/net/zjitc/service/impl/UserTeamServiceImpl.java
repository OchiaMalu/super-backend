package net.zjitc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.mapper.UserTeamMapper;
import net.zjitc.model.domain.UserTeam;
import net.zjitc.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * @author OchiaMalu
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2023-05-14 11:45:06
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




