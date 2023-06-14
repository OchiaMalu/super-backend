package net.zjitc.constants;

public interface RedisConstants {
    String RECOMMEND_KEY = "super:user:recommend";
    String REGISTER_CODE_KEY = "super:register:";
    Long REGISTER_CODE_TTL = 15L;
    String USER_UPDATE_PHONE_KEY = "suer:user:update:phone:";
    Long USER_UPDATE_PHONE_TTL = 15L;
    String USER_UPDATE_EMAIL_KEY = "suer:user:update:email:";
    Long USER_UPDATE_EMAIl_TTL = 15L;
    String USER_FORGET_PASSWORD_KEY = "super:user:forget:";
    Long USER_FORGET_PASSWORD_TTL = 15L;
}
