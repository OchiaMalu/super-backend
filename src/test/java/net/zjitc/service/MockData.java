package net.zjitc.service;

import net.zjitc.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Random;

@SpringBootTest
public class MockData {
    @Resource
    private UserService userService;
    @Test
    void insert() {
        String[] avatarUrls = {
                "http://rtrx7n2j6.hd-bkt.clouddn.com/12d4949b4009d089eaf071aef0f1f40.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/1bff61de34bdc7bf40c6278b2848fbcf.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/22fe8428428c93a565e181782e97654.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/75e31415779979ae40c4c0238aa4c34.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/905731909dfdafd0b53b3c4117438d3.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/a84b1306e46061c0d664e6067417e5b.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/b93d640cc856cb7035a851029aec190.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/c11ae3862b3ca45b0a6cdff1e1bf841.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/cccfb0995f5d103414bd8a8bd742c34.jpg",
                "http://rtrx7n2j6.hd-bkt.clouddn.com/f870176b1a628623fa7fe9918b862d7.jpg"};
        Random random=new Random();
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setUsername("坤"+getRandomString(5));
            user.setPassword("11111111");
            int randomInt = random.nextInt(10);
            user.setUserAccount(getRandomString(10));
            user.setAvatarUrl(avatarUrls[randomInt]);
            user.setRole(0);
            user.setGender(random.nextInt(2));
            user.setStatus(0);
            user.setIsDelete(0);
            userService.save(user);
        }
    }
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
