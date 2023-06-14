package net.zjitc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.UserLoginRequest;
import net.zjitc.model.request.UserRegisterRequest;
import net.zjitc.model.request.UserUpdateRequest;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.UserService;
import net.zjitc.utils.SMSUtils;
import net.zjitc.utils.ValidateCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.zjitc.constants.RedisConstants.*;
import static net.zjitc.constants.SystemConstants.EMAIL_FROM;
import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;


/**
 * 用户控制器
 *
 * @author 林哲好
 * @date 2023/05/15
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户管理模块")
public class UserController {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 字符串复述,模板
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * java邮件发送者
     */
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 发送消息
     *
     * @param phone   电话
     * @return {@link BaseResponse}<{@link String}>
     */
    @GetMapping("/message")
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "phone", value = "手机号")})
    public BaseResponse<String> sendMessage(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        String key = REGISTER_CODE_KEY + phone;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(code), REGISTER_CODE_TTL, TimeUnit.MINUTES);
//        System.out.println(code);
        SMSUtils.sendMessage(phone, String.valueOf(code));
        return ResultUtils.success("短信发送成功");
    }

    /**
     * 发送手机更新消息
     *
     * @param phone   电话
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @GetMapping("/message/update/phone")
    @ApiOperation(value = "发送手机号更新验证码")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "phone", value = "手机号"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> sendPhoneUpdateMessage(String phone, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        String key = USER_UPDATE_PHONE_KEY + phone;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(code), USER_UPDATE_PHONE_TTL, TimeUnit.MINUTES);
//        System.out.println(code);
        SMSUtils.sendMessage(phone, String.valueOf(code));
        return ResultUtils.success("短信发送成功");
    }

    /**
     * 发送邮件更新消息
     *
     * @param email   电子邮件
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     * @throws MessagingException 通讯异常
     */
    @GetMapping("/message/update/email")
    @ApiOperation(value = "发送邮箱更新验证码")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "email", value = "邮箱"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> sendMailUpdateMessage(String email, HttpServletRequest request) throws MessagingException {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (StringUtils.isBlank(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(new InternetAddress("SUPER <" + EMAIL_FROM + ">"));
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("SUPER 验证码");
        mimeMessageHelper.setText("我们收到了一项请求，要求更新您的邮箱地址为" + email + "。本次操作的验证码为：" + code + "。如果您并未请求此验证码，则可能是他人正在尝试修改以下 SUPER 帐号：" + loginUser.getUserAccount() + "。请勿将此验证码转发给或提供给任何人。");
        javaMailSender.send(mimeMessage);
        String key = USER_UPDATE_EMAIL_KEY + email;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(code), USER_UPDATE_EMAIl_TTL, TimeUnit.MINUTES);
//        System.out.println(code);
        return ResultUtils.success("ok");
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return {@link BaseResponse}<{@link Long}>
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "userRegisterRequest", value = "用户注册请求参数")})
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String phone = userRegisterRequest.getPhone();
        String code = userRegisterRequest.getCode();
        String account = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(phone, code, account, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(phone, code, account, password, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request          请求
     * @return {@link BaseResponse}<{@link User}>
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "userLoginRequest", value = "用户登录请求参数"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link Integer}>
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link User}>
     */
    @GetMapping("/current")
    @ApiOperation(value = "获取当前用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN);
            return ResultUtils.success(null);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 删除用户
     *
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "用户id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 搜索用户标签
     *
     * @param tagNameList 标记名称列表
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link User}>>
     */
    @GetMapping("/search/tags")
    @ApiOperation(value = "通过标签搜索用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tagNameList", value = "标签列表")})
    public BaseResponse<Page<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList, long currentPage) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<User> userList = userService.searchUsersByTags(tagNameList, currentPage);
        return ResultUtils.success(userList);
    }

    /**
     * 按用户名搜索用户
     *
     * @param username 用户名
     * @param request  请求
     * @return {@link BaseResponse}<{@link List}<{@link User}>>
     */
    @GetMapping("/search")
    @ApiOperation(value = "通过用户名搜索用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "username", value = "用户名"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<User>> searchUsersByUserName(String username, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 更新用户
     *
     * @param updateRequest 更新请求
     * @param request       请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "user", value = "用户更新请求参数"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> updateUser(@RequestBody UserUpdateRequest updateRequest, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isNotBlank(updateRequest.getEmail()) || StringUtils.isNotBlank(updateRequest.getPhone())) {
            if (StringUtils.isBlank(updateRequest.getCode())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入验证码");
            } else {
                userService.updateUserWithCode(updateRequest, loginUser.getId());
                return ResultUtils.success("ok");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        boolean success = userService.updateUser(user, request);
        if (success) {
            return ResultUtils.success("ok");
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }


    /**
     * 用户分页
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/page")
    @ApiOperation(value = "用户分页")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页")})
    public BaseResponse<Page<UserVO>> userPagination(long currentPage) {
        Page<UserVO> userVOPage = userService.userPage(currentPage);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 匹配用户
     *
     * @param currentPage 当前页面
     * @param request     请求
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/match")
    @ApiOperation(value = "获取匹配用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<UserVO>> matchUsers(long currentPage, HttpServletRequest request) {
        if (currentPage <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean isLogin = userService.isLogin(request);
        if (isLogin) {
            User user = userService.getLoginUser(request);
            return ResultUtils.success(userService.matchUser(currentPage, user));
        } else {
            return ResultUtils.success(userService.getRandomUser());
        }
    }

    /**
     * 得到用户id
     *
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link UserVO}>
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "用户id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<UserVO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.getUserById(id, loginUser.getId());
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户标签
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link String}>>
     */
    @GetMapping("/tags")
    @ApiOperation(value = "获取当前用户标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<String>> getUserTags(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<String> userTags = userService.getUserTags(loginUser.getId());
        return ResultUtils.success(userTags);
    }

    /**
     * 更新用户标签
     *
     * @param tags    标签
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PutMapping("/update/tags")
    @ApiOperation(value = "更新用户标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tags", value = "标签"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> updateUserTags(@RequestBody List<String> tags, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        userService.updateTags(tags, loginUser.getId());
        return ResultUtils.success("ok");
    }
}
