EasyHttp 一个可扩展的Http框架
===
一、 引入项目
---
compile 'com.liwy.easyhttp:easyhttp:1.0.5'

二、 初始化
---
在Application里初始化,主要初始化内容为统一解析回调和IHttpService实现类传入，具体如下：
```
         // 设置默认解析方式（已实现gson和xml的数据解析）
         DataParser.setDefaultParseType(DataParser.PARSE_GSON);
         // 也可以自定义解析方式，比如解析html类型的数据
         DataParser.addCallback("html", new Callback() {
             @Override
             public void onSuccess(String result, Class<?> clazz, SuccessCallback successCallback) {
                 // do parsing which you want to
             }
 
             @Override
             public void onError(String error, ErrorCallback errorCallback) {
                 // http error
             }
         });
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        OkHttpService okHttpService = new OkHttpService().init(okHttpClient);
        EasyHttp.getInstance().setHttpService(okHttpService);
```
或者
```
        RetrofitService retrofitService = new RetrofitService().init("http://192.168.131.19:8886/",10);
        EasyHttp.getInstance().setHttpService(retrofitService);
```
现实现了两种Http服务，分别是基于OkHttp和Retrofit，其中基于Retrofit的实现RetrofitService尚待完善。

三、 使用
---
1、 get
```
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        new EasyHttp.Builder()
                .setUrl("/login/update")
                .setTag("update")
                .setParseType(DataParser.PARSE_GSON)    // 设置数据的解析方式为GSON 也可为DataParser.PARSE_XML
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        contentTv.setText("Build String = " + result);
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(Object... values) {
                        contentTv.setText("请求失败");
                    }})
                .get();
```

2、post
```
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        new EasyHttp.Builder()
                .setUrl("/login/update")
                .setParseType(DataParser.PARSE_GSON)    // 设置数据的解析方式为GSON
                .setTag("update")   //请求的tag，可用于中断请求
                .setParams(params)
                .setSuccessCallback(new SuccessCallback<String>() {
                    @Override
                    public void success(String result) {
                        contentTv.setText("Build String = " + result);
                    }})
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void error(Object... values) {
                        contentTv.setText("请求失败");
                    }})
                .post();
```

如果需要在请求过程中取消该请求，可以如下操作：
```
    EasyHttp.getInstance().cancelHttp("update");
```

3、下载
```
 EasyHttp.getBuilder()
                .setFileUrl(url)
                .setFileName(fileName)
                .setFilePath(filePath)
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
                    public void onProgress(long total, long current) {
                        System.out.println("---->total=" + total + ",current=" + current);
                    }
            }).download();
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
            public void error(Object... values) {
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

更新日志
---
1.0.5（最新）
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
