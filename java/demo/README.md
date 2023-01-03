## 预备环境准备
AbcPenCloud SDK 依赖java 环境来运行，最低支持到Java8  
还需要为此配置[Maven](https://maven.apache.org/index.html)环境,
Demo项目使用的[Gradle](https://gradle.org/)

## 本地Demo
### 1. 新增maven 仓库
如果您的应用使用了 Maven，则在 pom.xml 文件中加入以下代码即可：

```xml
<id>myRepository1</id>
<repositories>
<repository>
    <!-- id必须唯一 -->
    <id>AbcPenCloud</id>
    <!-- 仓库的url地址 -->
    <url>https://nexus.abcpen.com/repository/cloud/</url>
    <releases>
        <enabled>true</enabled>
    </releases>

</repository>

...
</repositories>
```
### 2. 添加Maven引用
```maven

<dependency>
  <groupId>org.abcpen.cloud</groupId>
  <artifactId>asr-sdk</artifactId>
  <version>0.0.1</version>
</dependency>

```

### 3. 初始化AbcPenAiClient

```java
AbcPenAiClient abcPenAiClient = new AbcPenAiClient(APP_ID, APP_SECRETE);
```

完成以上三步后 您就可以使用AbcPenAiClient中的服务了



### 使用ASR服务

```java
//获取AsrService
AsrService asrService = abcPenAiClient.getAsrService();
```

#### AsrService说明

```java
/**
 * 同步识别
 *
 * @param asrRequest
 * @return
 */
AsrResult recognizeSync(AsrRequest asrRequest) throws AbcPenClientException;

/**
 * 同步识别
 *
 * @param asrRequest
 * @param timeout    超时时间
 * @return
 */
AsrResult recognizeSync(AsrRequest asrRequest, long timeout) throws AbcPenClientException;

/**
 * 异步识别
 *
 * @param asrRequest
 * @return
 * @throws AbcPenClientException
 */
void recognize(AsrRequest asrRequest, AbcPenCallBack<AsrResult> asrResultCallBack);


/**
 * 通过taskId 获取结果
 *
 * @param key
 * @param timeout
 * @return
 */
AsrResult getRecognizeResultByTaskId(String key, long timeout) throws AbcPenClientException;

/**
 * 创建task 此方法是为了 自己实现轮询获取结果
 *
 * @param asrRequest
 * @return
 */
String createRecognizeTask(AsrRequest asrRequest) throws AbcPenClientException;

/**
 * 回收
 */
void release();
```
#### AsrResult说明

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
| voice | string | 说话人区分标识 开通人声分离后 ,这里返回的对应声纹结果,注册对应声纹[声纹注册]() |
