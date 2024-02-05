package net.zjitc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.Team;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.TeamCoverUpdateRequest;
import net.zjitc.model.request.TeamJoinRequest;
import net.zjitc.model.request.TeamQueryRequest;
import net.zjitc.model.request.TeamQuitRequest;
import net.zjitc.model.request.TeamUpdateRequest;
import net.zjitc.model.vo.TeamVO;
import net.zjitc.model.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 团队服务
 *
 * @author OchiaMalu
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2023-05-12 19:33:37
 * @date 2024/01/25
 */
public interface TeamService extends IService<Team> {

    /**
     * 添加团队
     *
     * @param team      团队
     * @param loginUser 登录用户
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    long addTeam(Team team, User loginUser);

    /**
     * 列出团队
     *
     * @param currentPage 当前页码
     * @param teamQuery   团队查询
     * @param isAdmin     是否为管理员
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listTeams(long currentPage, TeamQueryRequest teamQuery, boolean isAdmin);

    /**
     * 更新团队
     *
     * @param teamUpdateRequest 团队更新请求
     * @param loginUser         登录用户
     * @return boolean
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入团队
     *
     * @param teamJoinRequest 团队加入请求
     * @param loginUser       登录用户
     * @return boolean
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出团队
     *
     * @param teamQuitRequest 团队退出请求
     * @param loginUser       登录用户
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除团队
     *
     * @param id        id
     * @param loginUser 登录用户
     * @param isAdmin   是否为管理员
     * @return boolean
     */
    boolean deleteTeam(long id, User loginUser, boolean isAdmin);

    /**
     * 获得团队
     *
     * @param teamId 团队id
     * @param userId 用户id
     * @return {@link TeamVO}
     */
    TeamVO getTeam(Long teamId, Long userId);

    /**
     * 列出我加入
     *
     * @param currentPage 当前页码
     * @param teamQuery   团队查询
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listMyJoin(long currentPage, TeamQueryRequest teamQuery);

    /**
     * 获取团队成员
     *
     * @param teamId 团队id
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> getTeamMember(Long teamId, Long userId);

    /**
     * 列出我所有加入
     *
     * @param id id
     * @return {@link List}<{@link TeamVO}>
     */
    List<TeamVO> listAllMyJoin(Long id);

    /**
     * 更改封面图像
     *
     * @param request 要求
     * @param userId  用户id
     * @param admin   管理
     */
    void changeCoverImage(TeamCoverUpdateRequest request, Long userId, boolean admin);

    /**
     * 踢出
     *
     * @param teamId      团队id
     * @param userId      用户id
     * @param loginUserId 登录用户id
     * @param admin       管理
     */
    void kickOut(Long teamId, Long userId, Long loginUserId, boolean admin);

    /**
     * 列出我创建
     *
     * @param currentPage 当前页码
     * @param userId      用户id
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listMyCreate(long currentPage, Long userId);

    /**
     * 获取已加入队员头像
     *
     * @param teamVoPage 团队vo分页
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> getJoinedUserAvatar(Page<TeamVO> teamVoPage);

}
