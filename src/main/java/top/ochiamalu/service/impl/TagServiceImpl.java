package top.ochiamalu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.mapper.TagMapper;
import top.ochiamalu.model.domain.Tag;
import top.ochiamalu.model.domain.TagChildren;
import top.ochiamalu.model.vo.TagVO;
import top.ochiamalu.service.TagService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 标签服务impl
 *
 * @author ochiamalu
 * @date 2025/04/15
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    private static final Gson GSON = new Gson();

    @Override
    public List<TagVO> getTags() {
        List<Tag> parentTags = this.lambdaQuery().list();
        if (parentTags.isEmpty()) {
            return Collections.emptyList();
        }
        return parentTags.stream().map(parentTag -> {

            TagVO tagVO = new TagVO();
            tagVO.setId(parentTag.getId());
            tagVO.setText(parentTag.getText());
            if (parentTag.getChildren() == null) {
                tagVO.setChildren(Collections.emptyList());
                return tagVO;
            }

            List<String> childrenTagList = GSON.fromJson(parentTag.getChildren(), new TypeToken<List<String>>() {
            }.getType());

            if (childrenTagList.isEmpty()) {
                return tagVO;
            }
            List<TagChildren> childrenList = new ArrayList<>();
            childrenTagList.forEach(childrenTag -> {
                TagChildren tagChildren = new TagChildren();
                tagChildren.setId(childrenTag);
                tagChildren.setText(childrenTag);
                childrenList.add(tagChildren);
            });
            tagVO.setChildren(childrenList);
            return tagVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean updateTag(Long id, List<String> tags) {
        Tag tag = this.getById(id);
        if (tag == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不存在");
        }
        if (tags == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String tagStr = GSON.toJson(tags);
        tag.setChildren(tagStr);
        return this.updateById(tag);
    }

    @Override
    public boolean addTag(String text) {
        Tag tag = new Tag();
        tag.setText(text);
        return this.save(tag);
    }
}
