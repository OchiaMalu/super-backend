package top.ochiamalu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ochiamalu.model.domain.Message;

/**
* @author OchiaMalu
* @description 针对表【message】的数据库操作Mapper
* @createDate 2023-06-21 17:39:30
 * @Entity top.ochiamalu.model.domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




