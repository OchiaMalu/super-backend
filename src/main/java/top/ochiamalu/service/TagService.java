package top.ochiamalu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ochiamalu.model.domain.Tag;
import top.ochiamalu.model.vo.TagVO;

import java.util.List;

/**
 * @author ochiamalu
* @description 针对表【tag】的数据库操作Service
 * @createDate 2025-04-15 11:53:40
*/
public interface TagService extends IService<Tag> {

    /**
     * 获取标签
     *
     * @return {@link List }<{@link TagVO }>
     */
    List<TagVO> getTags();

    /**
     * 更新标签
     *
     * @param id   ID
     * @param tags 标签
     * @return boolean
     */
    boolean updateTag(Long id, List<String> tags);

    /**
     * 添加标签
     *
     * @param text 文本
     * @return boolean
     */
    boolean addTag(String text);
}
