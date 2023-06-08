package net.zjitc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.model.domain.Blog;
import net.zjitc.model.domain.BlogComments;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.AddCommentRequest;
import net.zjitc.model.vo.BlogCommentsVO;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.BlogCommentsService;
import net.zjitc.mapper.BlogCommentsMapper;
import net.zjitc.service.BlogService;
import net.zjitc.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author OchiaMalu
 * @description 针对表【blog_comments】的数据库操作Service实现
 * @createDate 2023-06-08 12:44:45
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments>
        implements BlogCommentsService {

    @Resource
    private UserService userService;

    @Resource
    private BlogService blogService;

    @Override
    public void addComment(AddCommentRequest addCommentRequest, Long userId) {
        BlogComments blogComments = new BlogComments();
        blogComments.setUserId(userId);
        blogComments.setBlogId(addCommentRequest.getBlogId());
        blogComments.setContent(addCommentRequest.getContent());
        blogComments.setLikedNum(0);
        blogComments.setStatus(0);
        this.save(blogComments);
        Blog blog = blogService.getById(addCommentRequest.getBlogId());
        blogService.update().eq("id", addCommentRequest.getBlogId())
                .set("comments_num", blog.getCommentsNum() + 1).update();
    }

    @Override
    public List<BlogCommentsVO> listComments(long blogId) {
        LambdaQueryWrapper<BlogComments> blogCommentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogCommentsLambdaQueryWrapper.eq(BlogComments::getBlogId, blogId);
        List<BlogComments> blogCommentsList = this.list(blogCommentsLambdaQueryWrapper);
        return blogCommentsList.stream().map((comment) -> {
            BlogCommentsVO blogCommentsVO = new BlogCommentsVO();
            BeanUtils.copyProperties(comment, blogCommentsVO);
            User user = userService.getById(comment.getUserId());
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            blogCommentsVO.setCommentUser(userVO);
            return blogCommentsVO;
        }).collect(Collectors.toList());
    }
}




