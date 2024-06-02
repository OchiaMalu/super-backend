package top.ochiamalu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ochiamalu.mapper.UserTeamMapper;
import top.ochiamalu.model.domain.UserTeam;
import top.ochiamalu.service.UserTeamService;

/**
 * @author OchiaMalu
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2023-05-14 11:45:06
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




