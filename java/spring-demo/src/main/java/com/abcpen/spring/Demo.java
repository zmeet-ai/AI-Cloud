package com.abcpen.spring;

import com.abcpen.cloud.asr.AbcPenAiClient;
import com.abcpen.cloud.asr.model.tts.AudioPackage;
import com.abcpen.cloud.asr.service.AbcPenClient;
import com.abcpen.cloud.asr.service.AsrService;
import com.abcpen.cloud.asr.service.TTSService;
import com.abcpen.cloud.asr.service.VoicePrintService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @projectName: server
 * @className: Demo
 * @author: ZhaoCheng
 * @description:
 * @date: 2023/1/3 8:16 PM
 * @version: 1.0
 */
@Component
public class Demo implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(Demo.class);

    @Resource
    TTSService ttsService;

    @Resource
    AsrService asrService;

    @Resource
    AbcPenAiClient abcPenClient;

    @Resource
    VoicePrintService voicePrintService;

    @Override
    public void run(String... args) throws Exception {
        List<AudioPackage> audioPackageV2 = ttsService.getAudioPackageV2();
        logger.info(JSONObject.toJSONString(audioPackageV2));

    }
}
