package top.ochiamalu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ochiamalu.mapper.TagMapper;
import top.ochiamalu.model.domain.Tag;
import top.ochiamalu.service.TagService;

/**
 * @author OchiaMalu
 * @description 针对表【tag】的数据库操作Service实现
 * @createDate 2023-05-07 19:05:01
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




