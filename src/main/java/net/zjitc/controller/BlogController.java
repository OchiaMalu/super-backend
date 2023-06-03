package net.zjitc.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.BlogAddRequest;
import net.zjitc.service.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private BlogService blogService;

    @PostMapping("/add")
    public BaseResponse<String> addBlog(BlogAddRequest blogAddRequest, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (StringUtils.isAnyBlank(blogAddRequest.getTitle(), blogAddRequest.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        blogService.addBlog(blogAddRequest, loginUser);
        return ResultUtils.success("添加成功");
    }
}
