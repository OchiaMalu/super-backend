package net.zjitc.service.impl;

import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.model.domain.Blog;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.BlogAddRequest;
import net.zjitc.service.BlogService;
import net.zjitc.mapper.BlogMapper;
import net.zjitc.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author OchiaMalu
 * @description 针对表【blog】的数据库操作Service实现
 * @createDate 2023-06-03 15:54:34
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

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
}




