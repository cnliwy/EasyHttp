EasyHttp 一个可扩展的Http框架
===
一、使用
---
1、 引入项目
compile 'com.liwy.easyhttp:easyhttp:1.0.2'
2、 初始化
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
3、 使用<br/>
get
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

post
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
