package net.zjitc.service;

import net.zjitc.model.domain.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.FriendAddRequest;

/**
* @author OchiaMalu
* @description 针对表【friends(好友申请管理表)】的数据库操作Service
* @createDate 2023-06-18 14:10:45
*/
public interface FriendsService extends IService<Friends> {

    boolean addFriendRecords(User loginUser, FriendAddRequest friendAddRequest);
}
