package net.zjitc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.zjitc.model.domain.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.UserVO;

import java.util.List;

/**
* @author OchiaMalu
* @description 针对表【follow】的数据库操作Service
* @createDate 2023-06-11 13:02:31
*/
public interface FollowService extends IService<Follow> {

    void followUser(Long followUserId, Long userId);

    List<UserVO> listFans(Long userId);

    List<UserVO> listMyFollow(Long userId);

    Page<UserVO> pageMyFollow(Long userId,String currentPage);

    Page<UserVO> pageFans(Long userId, String currentPage);
}
