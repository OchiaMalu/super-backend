package net.zjitc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author OchiaMalu
* @description 针对表【user】的数据库操作Service
* @createDate 2023-05-07 19:56:01
*/
public interface UserService extends IService<User> {
    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    int userLogout(HttpServletRequest request);

    List<User> searchUsersByTags(List<String> tagNameList);

    boolean isAdmin(HttpServletRequest request);

    boolean updateUser(User user, HttpServletRequest request);

    Page<User> recommendUser(long currentPage);

    User getLoginUser(HttpServletRequest request);
}
