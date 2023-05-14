package net.zjitc.controller;

import cn.hutool.core.bean.BeanUtil;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.Team;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.TeamAddRequest;
import net.zjitc.model.request.TeamUpdateRequest;
import net.zjitc.service.TeamService;
import net.zjitc.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @PutMapping
    public BaseResponse updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamUpdateRequest, team);
        boolean updated = teamService.updateById(team);
        if (updated) {
            return ResultUtils.success(team.getId());
        } else {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "更新队伍出错");
        }
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(teamService.getById(id));
    }

    @GetMapping("/list")
    public BaseResponse listTeam() {
        return ResultUtils.success(teamService.list(null));
    }
}
