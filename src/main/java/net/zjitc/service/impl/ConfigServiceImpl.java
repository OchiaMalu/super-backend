package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.mapper.ConfigMapper;
import net.zjitc.model.domain.Config;
import net.zjitc.service.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OchiaMalu
 * @description 针对表【config】的数据库操作Service实现
 * @createDate 2024-04-08 11:53:41
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
        implements ConfigService {

    @Override
    public String getNoticeTest() {
        LambdaQueryWrapper<Config> configLambdaQueryWrapper = new LambdaQueryWrapper<>();
        configLambdaQueryWrapper.eq(Config::getType, 0);
        Config config = this.getOne(configLambdaQueryWrapper);
        return config.getValue();
    }

    @Override
    public List<String> getSwiperImgs() {
        LambdaQueryWrapper<Config> configLambdaQueryWrapper = new LambdaQueryWrapper<>();
        configLambdaQueryWrapper.eq(Config::getType, 1);
        List<Config> list = this.list(configLambdaQueryWrapper);
        return list.stream().map(Config::getValue).collect(Collectors.toList());
    }

    @Override
    public void updateNoticeText(String text) {
        LambdaQueryWrapper<Config> configLambdaQueryWrapper = new LambdaQueryWrapper<>();
        configLambdaQueryWrapper.eq(Config::getType, 0);
        this.remove(configLambdaQueryWrapper);
        Config config = new Config();
        config.setType(0);
        config.setValue(text);
        this.save(config);
    }

    @Override
    public void uploadImages(MultipartFile file) {


    }

    @Override
    public void removeUrl(String url) {
        LambdaUpdateWrapper<Config> configLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        configLambdaUpdateWrapper.eq(Config::getType, 1).eq(Config::getValue, url);
        this.remove(configLambdaUpdateWrapper);
    }
}




