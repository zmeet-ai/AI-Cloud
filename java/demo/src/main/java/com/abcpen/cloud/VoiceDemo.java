package com.abcpen.cloud;

import com.abcpen.cloud.asr.AbcPenAiClient;
import com.abcpen.cloud.asr.exception.AbcPenClientException;
import com.abcpen.cloud.asr.model.voice.VoiceSearchRequestByFile;
import com.abcpen.cloud.asr.model.voice.VoiceSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @projectName: server
 * @className: AsrDemo
 * @author: ZhaoCheng
 * @description: TODO
 * @date: 2023/1/3 7:17 PM
 * @version: 1.0
 */

public class VoiceDemo {

    static final Logger logger = LoggerFactory.getLogger(VoiceDemo.class);

    public static void main(String[] args) throws AbcPenClientException {
        AbcPenAiClient abcPenAiClient = new AbcPenAiClient(Cons.APP_ID, Cons.APP_SECRETE);
        /**
         * 根据文件注册
         */
        File file = new File(VoiceDemo.class.getResource("/test.wav").getFile());
//        RegisterVoiceByFileRequest request = new RegisterVoiceByFileRequest().setFile(file).setSpkName("女士1");
//        boolean b = abcPenAiClient.getVoicePrintService().registerVoiceSync(request);
//        logger.info("注册声纹 " + b);

        VoiceSearchRequestByFile voiceSearchRequestByFile = new VoiceSearchRequestByFile().setFile(file);
        List<VoiceSearchResult> voiceSearchResults = abcPenAiClient.getVoicePrintService().searchVoiceSync(voiceSearchRequestByFile);
//
//
        for (VoiceSearchResult voiceSearchResult : voiceSearchResults) {
            logger.info("搜索结果 :{}  分数: {}", voiceSearchResult.getSpkName(), voiceSearchResult.getScore());
        }
    }
}
