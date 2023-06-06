package net.zjitc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.Blog;
import net.zjitc.model.domain.BlogLike;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.BlogAddRequest;
import net.zjitc.model.vo.BlogVO;
import net.zjitc.service.BlogLikeService;
import net.zjitc.service.BlogService;
import net.zjitc.mapper.BlogMapper;
import net.zjitc.service.UserService;
import net.zjitc.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static net.zjitc.constants.SystemConstants.PAGE_SIZE;

/**
 * @author OchiaMalu
 * @description 针对表【blog】的数据库操作Service实现
 * @createDate 2023-06-03 15:54:34
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

    @Resource
    private BlogLikeService blogLikeService;

    @Resource
    private UserService userService;

    @Override
    public Boolean addBlog(BlogAddRequest blogAddRequest, User loginUser) {
        Blog blog = new Blog();
        ArrayList<String> imageNameList = new ArrayList<>();
        MultipartFile[] images = blogAddRequest.getImages();
        if (images != null) {
            for (MultipartFile image : images) {
                String filename = FileUtils.uploadFile(image);
                imageNameList.add(filename);
            }
            String imageStr = StringUtils.join(imageNameList, ",");
            blog.setImages(imageStr);
        }
        blog.setUserId(loginUser.getId());
        blog.setTitle(blogAddRequest.getTitle());
        blog.setContent(blogAddRequest.getContent());
        return this.save(blog);
    }

    @Override
    public Page<Blog> listMyBlogs(long currentPage, Long id) {
        if (currentPage <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.eq(Blog::getUserId, id);
        return this.page(new Page<>(currentPage, PAGE_SIZE), blogLambdaQueryWrapper);
    }

    @Override
    public void likeBlog(long blogId, Long userId) {
        //todo redis实现
        //todo 分布式锁
        Blog blog = this.getById(blogId);
        if (blog == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "博文不存在");
        }
        LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blogId);
        blogLikeLambdaQueryWrapper.eq(BlogLike::getUserId, userId);
        long isLike = blogLikeService.count(blogLikeLambdaQueryWrapper);
        if (isLike > 0) {
            blogLikeService.remove(blogLikeLambdaQueryWrapper);
            int newNum = blog.getLikedNum() - 1;
            this.update().eq("id", blogId).set("liked_num", newNum).update();
        } else {
            BlogLike blogLike = new BlogLike();
            blogLike.setBlogId(blogId);
            blogLike.setUserId(userId);
            blogLikeService.save(blogLike);
            int newNum = blog.getLikedNum() + 1;
            this.update().eq("id", blogId).set("liked_num", newNum).update();
        }
    }

    @Override
    public Page<BlogVO> pageBlog(long currentPage, Long userId) {
        Page<Blog> blogPage = this.page(new Page<>(currentPage, PAGE_SIZE));
        Page<BlogVO> blogVOPage = new Page<>();
        BeanUtils.copyProperties(blogPage, blogVOPage);
        //todo 设置博文首页图片，返回图片完整url
        if (userId == null) {
            return blogVOPage;
        }
        List<BlogVO> blogVOList = blogPage.getRecords().stream().map((blog) -> {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blog.getId());
            blogLikeLambdaQueryWrapper.eq(BlogLike::getUserId, userId);
            long isLike = blogLikeService.count(blogLikeLambdaQueryWrapper);
            blogVO.setIsLike(isLike > 0);
            return blogVO;
        }).collect(Collectors.toList());
        blogVOPage.setRecords(blogVOList);
        return blogVOPage;
    }

    @Override
    public BlogVO getBlogById(long blogId, Long userId) {
        Blog blog = this.getById(blogId);
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLikeLambdaQueryWrapper.eq(BlogLike::getUserId, userId);
        blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blogId);
        long isLike = blogLikeService.count(blogLikeLambdaQueryWrapper);
        blogVO.setIsLike(isLike > 0);
        User author = userService.getById(blog.getUserId());
        blogVO.setAuthor(author);
        return blogVO;
    }
}




