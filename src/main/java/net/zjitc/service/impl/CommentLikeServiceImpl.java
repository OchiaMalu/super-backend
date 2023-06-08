package net.zjitc.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.model.domain.BlogComments;
import net.zjitc.model.domain.CommentLike;
import net.zjitc.service.BlogCommentsService;
import net.zjitc.service.CommentLikeService;
import net.zjitc.mapper.CommentLikeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author OchiaMalu
 * @description 针对表【comment_like】的数据库操作Service实现
 * @createDate 2023-06-08 16:24:28
 */
@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike>
        implements CommentLikeService {

}




