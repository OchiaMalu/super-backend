package top.ochiamalu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.mapper.EmoticonMapper;
import top.ochiamalu.model.domain.Emoticon;
import top.ochiamalu.model.domain.User;
import top.ochiamalu.service.EmoticonService;
import top.ochiamalu.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表情符号服务实施
 *
 * @author ochiamalu
 * @date 2025/03/03
 */
@Service
public class EmoticonServiceImpl extends ServiceImpl<EmoticonMapper, Emoticon>
        implements EmoticonService {

    @Resource
    private UserService userService;

    @Override
    public List<String> getEmoticonList(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return this.lambdaQuery().eq(Emoticon::getUserId, loginUser.getId()).list()
                .stream().map(Emoticon::getUrl).collect(Collectors.toList());
    }

    @Override
    public boolean addEmoticon(String fileUrl, Long id) {
        Emoticon emoticon = new Emoticon();
        emoticon.setUserId(id);
        emoticon.setUrl(fileUrl);
        return this.save(emoticon);
    }

    @Override
    public boolean delete(String fileUrl, Long id) {
        return this.lambdaUpdate().eq(Emoticon::getUrl, fileUrl).eq(Emoticon::getUserId, id)
                .remove();
    }
}
