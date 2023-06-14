package net.zjitc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.zjitc.model.domain.User;

import java.util.List;

/**
* @author OchiaMalu
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-05-07 19:56:01
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<User> getRandomUser();
}




