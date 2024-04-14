package net.zjitc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.BlogComments;
import net.zjitc.model.request.AddCommentRequest;
import net.zjitc.model.vo.BlogCommentsVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author OchiaMalu
* @description 针对表【blog_comments】的数据库操作Service
* @createDate 2023-06-08 12:44:45
*/
public interface BlogCommentsService extends IService<BlogComments> {

    /**
     * 添加评论
     *
     * @param addCommentRequest 添加评论请求
     * @param userId            用户id
     */
    void addComment(AddCommentRequest addCommentRequest, Long userId);

    /**
     * 列出评论
     *
     * @param blogId 博客id
     * @param userId 用户id
     * @return {@link List}<{@link BlogCommentsVO}>
     */
    List<BlogCommentsVO> listComments(long blogId, long userId);

    /**
     * 获取评论
     *
     * @param commentId 议论id
     * @param userId    用户id
     * @return {@link BlogCommentsVO}
     */
    BlogCommentsVO getComment(long commentId, Long userId);

    /**
     * 点赞评论
     *
     * @param commentId 议论id
     * @param userId    用户id
     */
    @Transactional
    void likeComment(Long commentId, Long userId);

    /**
     * 删除评论
     *
     * @param id      id
     * @param userId  用户id
     * @param isAdmin 是否为管理员
     */
    void deleteComment(Long id, Long userId, boolean isAdmin);

    /**
     * 列出我评论
     *
     * @param id id
     * @return {@link List}<{@link BlogCommentsVO}>
     */
    List<BlogCommentsVO> listMyComments(Long id);

    /**
     * 分页我评论
     *
     * @param id          id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link BlogCommentsVO}>
     */
    Page<BlogCommentsVO> pageMyComments(Long id, Long currentPage);

    /**
     * 分页评论我的评论
     *
     * @param id          id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link BlogCommentsVO}>
     */
    List<BlogCommentsVO> pageMyCommented(Long id, Long currentPage);
}
