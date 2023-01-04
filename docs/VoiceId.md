
# Api文档

## Api概览

## 声纹相关接口

接口名称   | 接口功能 | 频率限制
| ------ | -------- | ---- |
| [注册声纹]()  | 注册声纹 | 20   |
| [搜索声纹]() | 声纹搜索 | 20   |

## 接口描述

本接口服务对用户声纹进行注册到声纹库，可搭配ASR识别 实现说话人区分  
• 支持用户自定义声纹名称，搜索声纹  
• 签名方法参考 [公共参数]()。  
• 接口请求域名： asr.cloud.abpen.com 。

## 注册声纹

**接口请求路径**:   `/ai/v1/voice/registerByFile` ,`/ai/v1/voice/registerByUrl`。  
**请求方式**: `Post  multipart/form-data ` , `Post JsonBody`

#### 输入参数

| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
| spkName | string | 是 | 声纹名 | xxx，不能超过32位字符
| file    | File  | 否  | 采样率16k、位长16bit、单声道 |
| url     | string | 否 | 通过url 方式注册声纹 采样率16k、位长16bit、单声道

**请求示例通过语音文件来调用接口**

```
POST /ai/v1/voice/registerByFile
Content-Type: multipart/form-data; boundary=4e7aadba-e6fd-487e-b10d-50a4cfcd4251
Content-Length: 225652
X-AP-TS: 1672750498
Host: asr.cloud.abcpen.cn
Authorization: V1-HMAC-SHA256;Scope=asr;Credential=0e90b9f323554cc0945efd6ea1609b32;Signature=85507663ac1dc44cb24e7eacebb6ce82ec968e86efe58de4c8f738e3ae97451e

--4e7aadba-e6fd-487e-b10d-50a4cfcd4251
Content-Disposition: form-data; name="file"; filename="test.wav"
....

--4e7aadba-e6fd-487e-b10d-50a4cfcd4251
Content-Disposition: form-data; name="spkName"
Content-Length: 7

...
--4e7aadba-e6fd-487e-b10d-50a4cfcd4251--


```

**返回示例**
```
{"success":true,"code":"0000","msg":"","timestamp":1672750941535}
```


**请求示例通过url来调用接口**
````
```
POST  /ai/v1/voice/registerByUrl HTTP/1.1
Host: asr.cloud.abcpen.com
Content-Type: application/json; charset=utf-8
X-AP-TC: 1672388975
Host: asr.cloud.abcpen.com
Authorization: V1-HMAC-SHA256,Asia/Shanghai;Scope=asr;Credential=test1;Signature=7d894a3a3aa1ee9c271a579d8b692e5224538924cf16549bf0334d0496190e1f
{  
 "spkName":"女士1",
 "url":"xxxx"
}
````

**返回示例**
```
{"success":true,"code":"0000","msg":"","timestamp":1672750941535}
```


## 搜索声纹

**接口请求路径**:   `/ai/v1/voice/searchVoiceByFile` ,`/ai/v1/voice/searchVoiceByUrl`。  
**请求方式**: `Post  multipart/form-data ` , `Post JsonBody`


#### 输入参数

| 字段名称 | 类型 | 必须 | 说明 | 示例 |
| --- | --- | --- |  --- |  --- | 
| file    | File  | 否  | 采样率16k、位长16bit、单声道 |
| url     | string | 否 | 通过url 方式注册声纹 采样率16k、位长16bit、单声道


#### 输出参数
| 字段名称 | 类型 |  说明 | 示例 |
| --- | --- | --- |  --- |
|score | double | 分数 | 99.99
| spkName | string | 声纹名称 | 女士1
|audioPath | string | 注册时使用的声音文件 |

**请求示例通过语音文件来调用接口**

```
POST /ai/v1/voice/searchVoiceByFile
Content-Type: multipart/form-data; boundary=80c2cae4-8d68-403b-ad9f-13c2cb2f6b87
Content-Length: 225534
X-AP-TS: 1672751382
Host: asr.cloud.abcpen.cn
Authorization: V1-HMAC-SHA256;Scope=asr;Credential=0e90b9f323554cc0945efd6ea1609b32;Signature=e19a43693e58b352743a3ce3bc8a0149ea46c77b96214ca377106e6920d67400

--80c2cae4-8d68-403b-ad9f-13c2cb2f6b87
Content-Disposition: form-data; name="file"; filename="test.wav"
Content-Length: 225358
....



```

**请求示例通过url来调用接口**

```
POST  /ai/v1/voice/registerByUrl HTTP/1.1
Host: asr.cloud.abcpen.com
Content-Type: application/json; charset=utf-8
X-AP-TC: 1672388975
Host: asr.cloud.abcpen.com
Authorization: V1-HMAC-SHA256,Asia/Shanghai;Scope=asr;Credential=test1;Signature=7d894a3a3aa1ee9c271a579d8b692e5224538924cf16549bf0334d0496190e1f
{  
 "url":"xxxx"
}
````

**返回示例**
```json
{  
    "success":true,  
    "code":"0000",  
    "msg":"",  
    "data":[  
        {  
            "score":99.288907996212615,  
            "spkName":"女士1",  
            "audioPath":"https://zos.abcpen.com/voiceid/abcpen/20230103/0717c65d-8120-42a2-9369-81180c821a27.wav"  
        },  
        {  
            "score":40.84291225148123,  
            "spkName":"江湖术士",  
            "audioPath":"https://zos.abcpen.com/voiceid/abcpen/20230103/a0620885-98bd-45a8-bbcb-4f6fa5b70189.wav"  
        },  
        {  
            "score":9.540072001787442,  
            "spkName":"赵成",  
            "audioPath":"https://zos.abcpen.com/voiceid/abcpen/20230103/c08228d1-a2be-49ac-8ae4-857ac88f9ad2.wav"  
        }  
    ],  
    "timestamp":1672751327652  
}
```
