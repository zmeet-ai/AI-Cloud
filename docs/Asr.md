# Api文档

# SDK使用
SDK                  | 描述           |
| ----------------------|--------------|
[Java](https://github.com/zmeet-ai/AI-Cloud/tree/main/java/demo)| Java Maven引用 |
[Spring](https://github.com/zmeet-ai/AI-Cloud/tree/main/java/spring-demo)| Maven引用             |
# Api概览

## 录音文件和视频识别相关接口

 接口名称                  | 接口功能        | 频率限制 | 队列数限制
|-----------------------| ----------- | ---- | --- |
| [创建识别任务](#创建识别任务)     | 音视频文件异步识别 | 20   | 10|
| [查询识别状态](#查询识别状态)     | 识别状态查询 | 20   | - |
| [获取识别结果](#获取识别结果)     | 识别结果获取 | 20 |  -|
| [获取任务队列列表](#获取任务队列列表) |当前识别队列列表| 20 | - |
| [取消识别](#取消识别)             | 取消识别 | 20 | - |



## 文件识别

### 接口描述

本接口服务对时长5小时以内的录音文件进行识别，异步返回识别全部结果。  
• 支持wav、mp3、m4a、flv、mp4、wma、3gp、amr、aac、ogg-opus、flac格式。  
• 支持语音 URL 和本地语音文件两种请求方式。语音 URL 的音频时长不能长于5小时，文件大小不超过3GB。本地语音文件调用不能大于5MB。  
• 提交录音文件识别请求后，在3小时内完成识别（大多数情况下1小时音频约3分钟以内完成识别，半小时内发送超过1000小时录音或者2万条识别任务的除外），识别结果在服务端可保存7天。  
• 支持回调或轮询的方式获取结果，结果获取请参考[ 录音文件识别结果查询](#获取识别结果)。  
• 生成字幕场景 [生成字幕最佳实践]()。  
• 签名方法参考 [公共参数](https://github.com/zmeet-ai/AI-Cloud/blob/main/java/docs/%E7%AD%BE%E5%90%8D.md)。  
• 接口请求域名： asr.cloud.abpen.com 。


默认接口请求频率限制：20次/秒，最大识别队列为10个，如您有提高请求频率限制的需求，请[联系客服人员](https://abcpen.com/)进行咨询。


### 创建识别任务

**接口请求路径**:   `/asr/v1/recognize/media` 。  
**请求方式**: `Post`
#### 输入参数

| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
|targetId  | string  | 否| 自定义标识Id 后续可以通过此id 查询状态 结果 保证唯一即可 | 123| 
| sourceType | int | 是 | 语音数据来源 0为url方式 1为postbody base64编码方式 |
| data | string | 否 | 当`sourceType`为1的时候 此值为对应 音频数据 base64编码且最大不能超过5M,超过5M的数据 请采用 url方式
| mediaUrl | string | 否 | 当`sourceType`为0的时候 此值为对应的url地址，媒体文件最大不能超过3G 支持wav、mp3、m4a、flv、mp4、wma、3gp、amr、aac、ogg-opus、flac
| enableSpeaker | boolean | 否 | 为true的时候 可以开启人声分离，需要开通人声分离服务
| speakerNumber | int |否| 当开启人声分离的时候 此值表示最大分离人数 取值范围0-10
| enableSummary | boolean | 否 | 开启识别结果的摘要提取, 需开通nlp 摘要提取服务
| filterDirty   | boolean | 否 | 是否开启脏词过滤，开启后脏词会通过*方式替换
| callBackUrl   | string | 否 | **暂未实现** 回调 URL，用户自行搭建的用于接收识别结果的服务URL。如果用户使用轮询方式获取识别结果，则无需提交该参数。回调格式&内容详见 [回调数据说明]()

#### 输出参数

**请求示例通过语音Url来调用接口**
```
POST /asr/v1/recognize/media HTTP/1.1
Host: asr.cloud.abcpen.com
Content-Type: application/json; charset=utf-8
X-AP-TC: 1672388975
Host: asr.cloud.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=asr;Credential=test1;Signature=7d894a3a3aa1ee9c271a579d8b692e5224538924cf16549bf0334d0496190e1f
{  
    "targetId": "123",
    "enableSpeaker":false,  
    "enableSummary":false,  
    "filterDirty":false,  
    "mediaUrl":"https://s3.abcpen.cn/zmeet/sdk/asr/media/25fde9e4-1e1e-41d4-8772-98ad7677c118.wav",  
    "sourceType":0,  
    "speakerNumber":0  
}
```
**返回示例**
```
{  
    "success":true,  
    "code":"0000",  
    "msg":"",  
    "data":{  
        "targetId":”123,  
        "taskId":"63aea172899f2d697dce3a2a"  
    },  
    "timestamp":1672388978568  
}
```

**请求示例通过语音数据调用接口**

```
POST  /asr/v1/recognize/media HTTP/1.1
Host: asr.cloud.abcpen.com
Content-Type: application/json; charset=utf-8
X-AP-TC: 1672388975
Host: asr.cloud.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=asr;Credential=test1;Signature=7d894a3a3aa1ee9c271a579d8b692e5224538924cf16549bf0334d0496190e1f
{  
    "targetId": "123",
    "enableSpeaker":false,  
    "enableSummary":false,  
    "filterDirty":false,  
    "data":"UklGRvi9AABXQVZF....省略..",  
    "sourceType":1,  
    "speakerNumber":0  
}
```
**返回示例**
```
{  
    "success":true,  
    "code":"0000",  
    "msg":"",  
    "data":{  
        "targetId":”123,  
        "taskId":"63aea172899f2d697dce3a2a"  
    },  
    "timestamp":1672388978568  
}
```



### 查询识别状态


**接口请求路径**:  `/asr/v1/recognize/status`

**请求方式**: `Get`

#### 输入参数
| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
| taskId | string | 否 | 通过创建任务的返回的taskId 查询识别状态 |
| targetId | string | 否 | 通过自定义的targetId 查询识别状态 |

以上`taskId` 和 `targetId` 必须传递其中一个

#### 输出参数
| 字段名称 | 类型 |  说明 | 示例 |
| --- | --- | --- |  --- 
| taskId | string | 任务id |
| status | string | 识别状态 -1=失败 0=识别中 100=完成|
| reason | string | 状态描述 |

**请求示例**

 ```
GET /asr/v1/recognize/status?taskId=63aea6a7899f2d697dce3a2e
X-AP-TS: 1672390311
Host: cloud-test.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=cloud-test;Credential=test1;Signature=ca6b858b6fb656085b6a43ba88739bc4c18592d41b1e3dbb94a01b84dcb471d1
 
 ```  

**返回实例**

```
{
    "success": true,
    "code": "0000",
    "msg": "",
    "data": {
        "taskId": "63aea8e1899f2d697dce3a30",
        "status": 0,
        "reason": "识别中"
    },
    "timestamp": 1672390890873
}
```

```
{
    "success": true,
    "code": "0000",
    "msg": "",
    "data": {
        "taskId": "63aea6a7899f2d697dce3a2e",
        "status": 100,
        "reason": "完成"
    },
    "timestamp": 1672390712237
}
```

### 获取识别结果

**接口请求路径**:  `/asr/v1/recognize/result`

**请求方式**: `Get`


#### 输入参数

| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
| taskId | string | 否 | 通过创建任务的返回的taskId 查询识别状态 |
| targetId | string | 否 | 通过自定义的targetId 查询识别状态 |

以上`taskId` 和 `targetId` 必须传递其中一个

#### 输出参数

| 字段名称 | 类型 |说明 | 示例 |
| --- | --- |   --- |  --- | 
| summaryInfo | object | 摘要，关键信息 (开启摘要提取后 此字段会返回对应数据)|
| recognizeWords | object | 识别结果信息 |

**summaryInfo 描述**

| 字段名称 | 类型 |说明 | 
| --- | --- |   --- | 
| sentences| string[] | 关键语句
| keywords | string[] | 关键字
| summary  | string | 文本摘要


**recognizeWords 描述**

| 字段名称 | 类型 | 说明                                          | 
| --- | --- |---------------------------------------------| 
| s| int | 开始时间 单位ms                                   |
| e | int | 结束时间 单位ms                                   |
| w | string | 识别结果文本                                      |
| voice | string | 说话人区分标识 开通人声分离后 ,这里返回的对应声纹结果,注册对应声纹[声纹注册](https://github.com/zmeet-ai/AI-Cloud/blob/main/java/docs/VoiceId.md) |

**请求示例**

 ```
GET /asr/v1/recognize/result?taskId=63aea6a7899f2d697dce3a2e
X-AP-TS: 1672390311
Host: cloud-test.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=cloud-test;Credential=test1;Signature=ca6b858b6fb656085b6a43ba88739bc4c18592d41b1e3dbb94a01b84dcb471d1
 
 ```

**返回示例**

```
{  
    "success":true,  
    "code":"0000",  
    "msg":"",  
    "data":{  
        "taskId":"63aeab66899f2d697dce3a34",  
        "targetId":"123",  
        "summaryInfo":{  
            "sentences":[  
                "离离原上草，一岁一枯荣",  
                "野火烧不尽，春风吹又生"  
            ],  
            "keywords":[  
                "植物",  
                "古诗"  
            ],  
            "summary":"春暖花开,春风吹起。"  
        },  
        "recognizeWords":[  
            {  
                "s":400,  
                "e":1680,  
                "w":"离离原上草，",  
                "voice":"1"  
            },  
            {  
                "s":1680,  
                "e":3000,  
                "w":"一岁一枯荣。",  
                "voice":"1"  
            },  
           {  
                "s":3000,  
                "e":4320,  
                "w":"野火烧不尽，",  
                "voice":"1"  
            },  
            {  
                "s":4320,  
                "e":5460,  
                "w":"春风吹又生。",  
                "voice":"1"  
            }  
        ]  
    },  
    "timestamp":1672391536752  
}
```



### 获取任务队列列表

**接口请求路径**:  `/asr/v1/recognize/queues`

**请求方式**: `Get`

#### 输入参数

无

#### 输出参数

| 字段名称 | 类型 |  说明 | 示例 |
| --- | --- | --- |  --- 
| taskId | string | 任务id |
| status | string | 识别状态|

**请求示例**

 ```
GET /asr/v1/recognize/queues
X-AP-TS: 1672390311
Host: cloud-test.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=cloud-test;Credential=test1;Signature=ca6b858b6fb656085b6a43ba88739bc4c18592d41b1e3dbb94a01b84dcb471d1
 
 ```  


**返回示例**


 ```
 {
    "success": true,
    "code": "0000",
    "msg": "",
    "data": [
        {
            "taskId": "63aeb2878ccd03483ed9154d",
            "status": 0
        }
    ],
    "timestamp": 1672393352374
}
```


### 取消识别

**接口请求路径**:  `/asr/v1/recognize/cancel`


**请求方式**: `Post`


#### 输入参数

| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
| taskId | string | 否 | 通过创建任务的返回的taskId 查询识别状态 |
| targetId | string | 否 | 通过自定义的targetId 查询识别状态 |


以上`taskId` 和 `targetId` 必须传递其中一个


**请求示例**

```
POST /asr/v1/recognize/cancel HTTP/1.1
Host: asr.cloud.abcpen.com
Content-Type: application/json; charset=utf-8
X-AP-TC: 1672388975
Host: asr.cloud.abcpen.com
Authorization: V1-HMAC-SHA256;Scope=asr;Credential=test1;Signature=7d894a3a3aa1ee9c271a579d8b692e5224538924cf16549bf0334d0496190e1f
{  
    //以下值传递任何一个即可
    "targetId": "XXXX",
    "taskId":"XXXXX"
   
}
```


**返回示例**

```
{  
    "success":true,  
    "code":"0000",  
    "msg":""
}
```




### 错误码

以下仅列出了接口业务逻辑相关的错误码，其他错误码详见[公共错误码]()。

| 错误码 | 描述                           |
| --- |------------------------------|
| -1 | 识别失败                         |
 | 11000| 任务不存在                        |
| 11001| 参数错误                         |
| 11002 | 识别中. 此code返回在获取识别结果时 任务未识别完成 |


