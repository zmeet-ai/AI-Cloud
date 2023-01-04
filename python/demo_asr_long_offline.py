#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import asyncio
import sys
import json
import logging
import time
import argparse
from progress.bar import Bar
from urllib.parse import urlencode

from auth.client_auth_service import get_auth

def voiceid_register():
    global args
    
    parser = argparse.ArgumentParser(description="ASR Server offline audio file demo",
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('-u', '--url', type=str, metavar='URL',
                        help='server url', default='asr.cloud.abcpen.cn')
    args = parser.parse_args()
    # 下面的app_id 和api_key仅供测试使用，生产环境请向商务申请(手机：18605811078, 邮箱：jiaozhu@abcpen.com)
    app_id = "test1"
    app_secret = "2258ACC4-199B-4DCB-B6F3-C2485C63E85A"
    if (len(app_id) <= 0 or len(app_secret) <= 0):
        print("Please apply appid and appsecret, demo will exit now")
        sys.exit(1)
    timestamp = str(int(time.time()))

    signa = get_auth('asr',timestamp, app_id, app_secret)

    headers = {
       "X-AP-TS":timestamp,
       "Authorization":signa
    }
    files = {'file': open('./dataset/test.wav', 'rb')}
    values = {'spkName': '测试女声', 'tag_id': 'Ms.'}
    url = "https://{}/ai/v1/voice/registerByFile".format(args.url)
    response = requests.post(url, files=files, data=values,headers=headers)
    print(response.request.headers)
    return response.text



async def asr_offline(url_wave):
    global args

    parser = argparse.ArgumentParser(description="ASR Server offline audio file demo",
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('-u', '--url', type=str, metavar='URL',
                        help='server url', default='asr.cloud.abcpen.cn')
    parser.add_argument('-l', '--log_path', type=str, metavar='LOG',
                        help='log file path', default='asr_res.log')
    args = parser.parse_args()

    # 下面的app_id 和api_key仅供测试使用，生产环境请向商务申请(手机：18605811078, 邮箱：jiaozhu@abcpen.com)
    app_id = "test1"
    app_secret = "2258ACC4-199B-4DCB-B6F3-C2485C63E85A"
    if (len(app_id) <= 0 or len(app_secret) <= 0):
        print("Please apply appid and appsecret, demo will exit now")
        sys.exit(1)
    timestamp = str(int(time.time()))

    signa = get_auth('asr',timestamp, app_id, app_secret)
    print("signa {}".format(signa))

    headers = {
       "X-AP-TS":timestamp,
       "Authorization":signa,
       "content-type": "application/json"
    }

    query_post_apply = {
        "mediaUrl": url_wave,
        "sourceType":0,
        # 开启说话人分离 ，这里需要提前注册对应用户声纹才可以 查看 demo_voice.py文件
        "enableSpeaker": True,
        # 开启摘要提取
        "enableSummary": True
    }
    
    url = "https://{}/asr/v1/recognize/media".format(args.url)

   
    response = requests.post(url, data=json.dumps(query_post_apply), headers=headers)
    print(response.text)

    response_json = json.loads(response.text)

    query_result = {
        
    }
    query_result["taskId"] = response_json["data"]["taskId"]


    url = "https://{}/asr/v1/recognize/result".format(args.url)

    response2 = requests.get(url, query_result,headers=headers)
    response_json = json.loads(response2.text)
    bar = Bar('Processing', max=100)
    success = response_json["success"]
    while (success!=True):
        bar.next()
        await asyncio.sleep(3)
        response2 = requests.get(url, query_result,headers=headers)
        response_json = json.loads(response2.text)

        success = response_json["success"]
        if (success!=True):
            print(response_json)
            """in progress"""
            bar.next()
            continue

        return response_json

    else:
        response_json = json.loads(response2.text)
        return ''


async def main():
    try:

        print("=======注册声纹============")
        register=voiceid_register()
        print(register)
    
        print("=======异步识别============")
        result = await asyncio.gather(asr_offline("https://s3.abcpen.cn/zmeet/sdk/asr/media/3010c363-b510-47d0-ad97-f652657d6843.wav"))
        print(json.dumps(result,ensure_ascii=False, sort_keys=True, indent=4, separators=(', ', ': ')))

    except KeyboardInterrupt:
        pass
    await result

if __name__ == "__main__":
    try:
        print("长语音离线识别演示, 演示异步提交识别 轮询获取结果方式")
        asyncio.run(main())
    except Exception as e:
        logging.info("Got ctrl+c exception-2: %s, exit process", repr(e))