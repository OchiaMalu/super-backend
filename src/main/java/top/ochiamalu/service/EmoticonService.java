package top.ochiamalu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ochiamalu.model.domain.Emoticon;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 表情符号服务
 *
 * @author ochiamalu
 * @date 2025/03/03
 */
public interface EmoticonService extends IService<Emoticon> {

    /**
     * 获取表情
     *
     * @param request 请求
     * @return {@link List }<{@link String }>
     */
    List<String> getEmoticonList(HttpServletRequest request);

    /**
     * 添加表情
     *
     * @param fileUrl 文件 URL
     * @param id      ID
     * @return boolean
     */
    boolean addEmoticon(String fileUrl, Long id);

    /**
     * 删除
     *
     * @param fileUrl 文件 URL
     * @param id      ID
     * @return boolean
     */
    boolean delete(String fileUrl, Long id);
}
