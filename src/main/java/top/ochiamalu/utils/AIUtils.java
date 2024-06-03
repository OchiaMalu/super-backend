package top.ochiamalu.utils;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.stereotype.Component;
import top.ochiamalu.properties.AIProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static top.ochiamalu.constants.AIConstants.MAX_TOKENS;

/**
 * AI 工具类
 *
 * @author OchiaMalu
 * @date 2024/06/01
 */
@Component
public class AIUtils {

    private static AIProperties aiProperties;

    private static ClientV4 client;

    @Resource
    private ClientV4 tempClient;

    @Resource
    private AIProperties tempProperties;

    /**
     * 获取AI消息
     *
     * @param userMessage 用户消息
     * @return {@link String }
     */
    public static String getAIMessage(String userMessage) {
        if (Boolean.FALSE.equals(aiProperties.getEnable())) {
            return "AI服务未启用";
        }
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .maxTokens(MAX_TOKENS)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return (String) invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent();
    }

    @PostConstruct
    public void init() {
        aiProperties = tempProperties;
        client = tempClient;
    }
}
