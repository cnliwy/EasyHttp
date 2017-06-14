EasyHttp 一个可扩展的Http框架
===
一、 引入项目
---
compile 'com.liwy.easyhttp:easyhttp:1.0.2'

二、 初始化
---
在Application里初始化如下：
```
OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
OkHttpService okHttpService = new OkHttpService().init(okHttpClient);
EasyHttp.getInstance().setHttpService(okHttpService);
```
或者
```
RetrofitService retrofitService = new RetrofitService().init("http://192.168.131.19:8886/",10);
EasyHttp.getInstance().setHttpService(retrofitService);
```
现实现了两种Http服务，分别是基于OkHttp和Retrofit。

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

更新日志
---
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
