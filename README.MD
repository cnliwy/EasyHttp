EasyHttp 
===
一、 引入项目
---
compile 'com.liwy.easyhttp:easyhttp:1.0.9'

二、 初始化
---
在Application里初始化,主要初始化内容为统一解析回调和IHttpService实现类传入，具体如下：
```
        // 实例化请求实现类
         OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
         RequestService requestService = new RequestService(okHttpClient);
         // 设置EasyHttp的功能实现类为okHttpService，post提交方式为form表单，数据解析方式为GSON
         EasyHttp.getInstance().initHttpService(requestService, Constants.MEDIA_TYPE_FORM,DataParser.PARSE_GSON);
```
如需在回调前对数据或业务进行自定义处理，可实现拦截器，如下;
```
EasyHttp.getInstance().addIntercepor(new Interceptor() {
            @Override
            public boolean processSuccess(Object obj) {
                if (obj instanceof List){
                    System.out.println("-------------------->是的没错，确实是集合");
                    return true;//拦截下一步操作，不执行successCallback回调
                }
                return false;//不拦截，执行successCallback回调
            }

            @Override
            public boolean processError(String error) {
                return false;
            }
        });
```
如需自定义数据解析方式，需实现数据解析类的接口，并加入解析回调集合：
```
        // 默认已实现两种解析方式GSON和xml
         // 也可以自定义解析方式，比如解析html类型的数据
        EasyHttp.getInstance().addDataParse("html", new DataProcessor() {
                    @Override
                    public void onSuccess(String result,SuccessCallback successCallback) {
                        // do parsing which you want to
                    }
        
                    @Override
                    public void onError(String error, ErrorCallback errorCallback) {
                        // http error
                    }
                });
```

三、 使用
---
目前共提供四种方法，如下：
1. get(EasyRequest req)
2. post(EasyRequest req)
3. download(EasyRequest req)
4. upload(EasyRequest req)
调用：EasyHttp.getInstance().post(req);

1、 get请求
```
       Map<String, Object> params = new HashMap<>();
              params.put("identity", "40283c825d2bca81015d2bcabe850000");
              params.put("jsonKey", "test");
              EasyRequest easyRequest = EasyHttp.getBuilder()
                      .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                      .setParams(params)
                      .setTag("testTag")
                      .setSync(true) //设置本次请求为同步,默认为异步请求.
                      .setSuccessCallback(new SuccessCallback<String>() {
                          @Override
                          public void success(String result) {
                              System.out.println(result);
                              tvContent.setText(result);
                          }
                       })
                      .build();
              EasyHttp.getInstance().get(easyRequest);
```
参数也可不通过map直接传入，如下
```
EasyRequest easyRequest = EasyHttp.getBuilder()
                      .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                      .addParam("identity", "40283c825d2bca81015d2bcabe850000")
                      .addParam("jsonKey", "test")
                      .setTag("testTag")
                      .setSync(false) 
                      .setSuccessCallback(new SuccessCallback<String>() {
                          @Override
                          public void success(String result) {
                              System.out.println(result);
                              tvContent.setText(result);
                          }
                       })
                      .build();
```
如果需要在请求过程中取消该请求，可以如下操作：
```
    EasyHttp.getInstance().cancelHttp("testTag");
```


2、post请求
```
      EasyRequest easyRequest = EasyHttp.getBuilder()
                      .setUrl("http://192.168.131.19:8080/cnliwy/appdata/getTestData")
                      .addParam("identity", "40283c825d2bca81015d2bcabe850000")
                      .addParam("jsonKey", "getDatas")
                      .post()
                      .setSuccessCallback(new SuccessCallback<List<Data>>() {
                          @Override
                          public void success(List<Data> result) {
                              for (Data data : result){
                                  System.out.println(data.getName());
                              }
                              tvContent.setText("成功获取数据：" + result.toString());
                          }
                      })
                      .setErrorCallback(new ErrorCallback() {
                          @Override
                          public void error(String errorMsg) {
                              System.out.println("请求失败");
                          }
                      })
                      .build();
              EasyHttp.getInstance().http(easyRequest);
```


3、下载
```
         String url = "http://img5q.duitang.com/uploads/item/201506/23/20150623203928_HzBWU.jpeg";
         String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/img";
         String fileName = "moon.jpeg";
         EasyRequest easyRequest = EasyHttp.getBuilder()
                 .setUrl(url)
                 .setFileName(fileName)
                 .setTag(tag)
                 .setSaveDir(filePath)
                 .setDownloadCallback(new DownloadCallback<File>() {
                     @Override
                     public void onSuccess(File o) {
                         System.out.println("---->下载成功" + o.getAbsolutePath());
                     }
 
                     @Override
                     public void onError(String err) {
                         System.out.println("---->下载失败");
                     }
 
                     @Override
                     public void onProgress(long total, int progress) {
                         tvContent.setText("已下载%" + progress);
                         if(progress == 100)tvContent.setText("下载完成");
                     }
                 })
                 .build();
         EasyHttp.getInstance().download(easyRequest);
```

4、上传
```
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("title","upload head icon and apk");
        params.put("uploadUser","cnliwy");
        params.put("uploadType","image and apk");
        
        List<EasyFile> files = getFiles();

        EasyHttp.getBuilder().setUrl(url).setParams(params).setFiles(files).setSuccessCallback(new SuccessCallback() {
            @Override
            public void success(Object result) {
                System.out.println("上传成功，" + result);
                contentTv.setText("上传成功，" + result);
            }
        }).setErrorCallback(new ErrorCallback() {
            @Override
            public void error(String errorMsg) {
                System.out.println(values[0].toString());
                contentTv.setText("上传失败"+ values[0].toString());
            }
        }).postFile();
        // 需要上传的文件
        public List<EasyFile> getFiles(){
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/aliwy/";
                List<EasyFile> files = new ArrayList<>();
                files.add(new EasyFile("image1",filePath + "guide_one.png","image/png",new File(filePath + "guide_one.png")));
                files.add(new EasyFile("image2",filePath + "guide_two.png","image/png",new File(filePath + "guide_two.png")));
                return files;
         }
```

四 EasyRequest方法
---
1. 必传
```
        /**
         * 设置本次请求的url
         * @param url
         * @return
         */
        public Builder setUrl(String url);
        
```
2. 可选
```
        /**
         * 传入context对象
         * @param context
         * @return
         */
        public Builder setContext(Context context);

        /**
         * 添加http的header集合
         * @param headers
         * @return
         */
        public Builder setHeaders(Map<String, String> headers);

        /**
         * 传入自定义的okhttpclient
         * @param okHttpClient
         * @return
         */
        public Builder setOkHttpClient(OkHttpClient okHttpClient);

     
             /**
              * 是否打印日志
              * @param log
              * @return
              */
             public Builder isLog(boolean log) {
                 isLog = log;
                 return this;
             }

        /**
         * 设置用于取消本次请求的tag
         * @param tag
         * @return
         */
        public Builder setTag(Object tag);
        

        /**
         * 添加请求参数集合
         * @param params
         * @return
         */
        public Builder setParams(Map<String, Object> params);

        /**
         * 添加请求参数和值
         * @param key
         * @param value
         * @return
         */
        public Builder addParam(String key,Object value);

        /**
         * 添加http的header信息
         * @param key
         * @param value
         * @return
         */
        public Builder addHeader(String key,String value);

        /**
         * 传入要上传的文件
         * @param uploadFiles
         * @return
         */
        public Builder setUploadFiles(List<EasyFile> uploadFiles)



         /**
         * 自定义requestbody数据，若用此方法，则setMediaTpe和setParam\setParams则不需设置
         * @param requestBody
         * @return
         */
        public Builder requestBody(RequestBody requestBody);
       
        /**
         * 设置post提交数据的mediaType类型，默认为form。需与服务器保持一致。
         * @param mediaType  form或json
         * @return
         */
        public Builder setMediaType(String mediaType);

        /**
         * 设置本次请求为同步或异步。默认为异步。
         * @param sync
         * @return
         */
        public Builder setSync(boolean sync) ;

        /**
         * 设置本次的数据解析方式，xml解析或者gson解析，默认gson解析
         * @param parseType  xml或者gson,也可是自定义的解析类型。
         * @return
         */
        public Builder setParseType(String parseType);

        /**
         * 设置文件下载的存储路径
         * @param saveDir
         * @return
         */
        public Builder setSaveDir(String saveDir) ;

        /**
         * 设置文件的存储清楚，不传则默认采用下载链接里的文件名称。
         * @param fileName
         * @return
         */
        public Builder setFileName(String fileName);

        /**
         * 请求成功的结果回调
         * @param successCallback
         * @return
         */
        public Builder setSuccessCallback(SuccessCallback successCallback);

        /**
         * 请求失败的结果回调
         * @param errorCallback
         * @return
         */
        public Builder setErrorCallback(ErrorCallback errorCallback);

        /**
         * 下载的结果回调
         * @param downloadCallback
         * @return
         */
        public Builder setDownloadCallback(DownloadCallback downloadCallback);
```

EasyRequest.Builder使用注意
---
1. EasyRequest.Builder里除了setUrl是必须的外，其余方法均根据具体情况选择性调用。
2. 如果需自定义post的请求体，需调用requestBody()；如果调用了此方法则不需再调用setParam和setMediaType
3. 如需使当前请求为同步请求，需调用sync(true)
4. addParam和setParams并不冲突，可同时调用，不会覆盖已传入数据。addHeader和setHeaders同理。
5. 如果需要在网络请求中取消请求，需提前调用setTag()设置tag值，可通过调用EasyHttp.getInstance().cancelHttp(tag)取消请求。
6. download方法，如果存储路径已存在该文件，则直接进入下载成功的回调。
7. 目前数据解析已实现xml和gson两种方式。可通过setParseType("xml")主动设置本次请求的解析方式。
8. 如果设置为同步请求则需在子线程里发起（主线程不允许同步请求）。请求所在的线程里的方法均为同步。

更新日志
---
1.0.9(最新)
---
1. 修复无拦截器时无法成功回调请求结果的bug。
2. 新增请求日志打印，默认不打印,可通过EasyHttp的实例isLog(boolean)配置全局，也可以通过EasyRequest配置单次请求的行为。


1.0.8
---
1. 完善同步请求
2. 优化上传方法
3. 新增拦截器，可在成功失败回调前进行自定义处理
4. 框架的初始化操作全部调整在EasyHttp的实例里进行

1.0.7
---
1. 自定义EasyRequest请求体
2. 删除OkHttpService和HttpProxyService

1.0.6
---
1. 实现泛型类的自定义解析，解除对GSON的依赖
2. 取消EasyHttp对IHttpService的实现，采用HttpProxyService代理类实现功能
3. EasyHttp初始化方法更改为initHttpService，并新增两个参数，分别设置post请求的默认ContentType和默认的数据解析方式parseType


1.0.5
---
1. 源码分为两个分支，其中release为发布版分支
2. 清理release分支下retrofit的实现及依赖包

1.0.4
---
1. 新增文件上传功能(OkHttpService已实现)
2. OkHttpService实现主线程回调
3. 新增可扩展数据解析回调，默认GSON解析

1.0.3
---
1. 新增下载方法,OkHttpService和RetrofitService均已实现。
2. EasyHttp新增获取Builder的静态方法
    public static Builder getBuilder():
3. 删除无用类
    
1.0.2
---
1. 完善EasyHttp初始化方法
2. 新增Http请求中断功能
3. 修复bug.

1.0.1
---
1. 1.0.1版本发布至Jcenter.
