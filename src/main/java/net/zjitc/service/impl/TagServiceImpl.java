package net.zjitc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.mapper.TagMapper;
import net.zjitc.model.domain.Tag;
import net.zjitc.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @author OchiaMalu
 * @description 针对表【tag】的数据库操作Service实现
 * @createDate 2023-05-07 19:05:01
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




