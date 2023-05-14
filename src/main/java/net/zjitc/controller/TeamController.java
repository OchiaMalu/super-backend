package net.zjitc.controller;

import cn.hutool.core.bean.BeanUtil;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.Team;
import net.zjitc.model.domain.User;
import net.zjitc.model.dto.TeamQuery;
import net.zjitc.model.request.TeamAddRequest;
import net.zjitc.model.request.TeamJoinRequest;
import net.zjitc.model.request.TeamUpdateRequest;
import net.zjitc.model.vo.TeamUserVO;
import net.zjitc.service.TeamService;
import net.zjitc.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static net.zjitc.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping
    public BaseResponse addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtil.copyProperties(teamAddRequest, team);
        return ResultUtils.success(teamService.addTeam(team, loginUser));
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteTeam(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean removed = teamService.removeById(id);
        if (removed) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "删除队伍出错");
        }
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }


    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(teamService.getById(id));
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(teamService.listTeams(teamQuery, userService.isAdmin(loginUser)));
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

}
