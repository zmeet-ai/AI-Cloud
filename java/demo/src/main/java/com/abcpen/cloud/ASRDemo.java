package com.abcpen.cloud;

import com.abcpen.cloud.asr.AbcPenAiClient;
import com.abcpen.cloud.asr.exception.AbcPenClientException;
import com.abcpen.cloud.asr.listener.AbcPenCallBack;
import com.abcpen.cloud.asr.model.asr.AsrRequest;
import com.abcpen.cloud.asr.model.asr.AsrResult;
import com.abcpen.cloud.asr.model.asr.RecognizeMo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @projectName: server
 * @className: AsrDemo
 * @author: ZhaoCheng
 * @description: TODO
 * @date: 2023/1/3 7:17 PM
 * @version: 1.0
 */

public class ASRDemo {

    static final Logger logger = LoggerFactory.getLogger(ASRDemo.class);

    public static void main(String[] args) throws AbcPenClientException, InterruptedException {
        AbcPenAiClient abcPenAiClient = new AbcPenAiClient(Cons.APP_ID, Cons.APP_SECRETE);
        AsrRequest asrRequest = new AsrRequest();
        asrRequest.setSourceType(0);
        asrRequest.setMediaUrl("https://cos.abcpen.com/asr/video/fde7e9162d1049778e298239730a099e.mp4");
        //开启人声分离
        asrRequest.setEnableSpeaker(true);

        //同步识别返回taskId
        // String recognizeTask = abcPenAiClient.getAsrService().createRecognizeTask(asrRequest);
        //通过taskId 检索结果
        // AsrResult recognizeResultByTaskId = abcPenAiClient.getAsrService().getRecognizeResultByTaskId(recognizeTask, 10);

        //异步识别
        CountDownLatch countDownLatch = new CountDownLatch(1);
        abcPenAiClient.getAsrService().recognize(asrRequest, new AbcPenCallBack<AsrResult>() {
            @Override
            public void onSuccess(AsrResult asrResult) {
                List<RecognizeMo> recognizeWords = asrResult.getRecognizeWords();
                String collect = recognizeWords.stream().map(new Function<RecognizeMo, String>() {
                    @Override
                    public String apply(RecognizeMo recognizeMo) {
                        StringBuilder sb = new StringBuilder("时间:").append(recognizeMo.getS()).append("---->").append(recognizeMo.getE())
                                .append("\n")
                                .append("说话人:")
                                //如果注册了声纹 返回对应的spkName 如果未注册声纹 则为空
                                .append(recognizeMo.getVoice())
                                .append("\n")
                                .append("识别结果:")
                                .append(recognizeMo.getW())
                                .append("\n\n");
                        return sb.toString();
                    }
                }).collect(Collectors.joining("\n"));

                logger.info("识别结果 \n{}", collect);
                countDownLatch.countDown();
            }

            @Override
            public void onError(AbcPenClientException clientException, String reason) {
                logger.error("识别失败 {}", reason, clientException);
                countDownLatch.countDown();
            }
        });

        logger.info("等待识别完成 .....");
        countDownLatch.await();
        logger.info("识别完成");
    }
}
