package com.liwy.test;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.liwy.easyhttp.callback.OnSuccessCallback;
import com.liwy.test.bean.Data;
import com.liwy.test.bean.TestResult;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void parseData(){
        String jsonStr = "{ \"name\": \"liwy\", \"url\": \"www.baidu.com\"}";
        Data data = new Gson().fromJson(jsonStr,Data.class);
        System.out.println(data.toString());
    }
    @Test
    public void testType(){
        OnSuccessCallback<List<Data>> onSuccessCallback = new OnSuccessCallback<List<Data>>() {
            @Override
            public void success(List<Data> result) {

            }
        };
        Type type = getSuperclassTypeParameter(onSuccessCallback.getClass());
//        System.out.println(type.toString());
    }
    public Type getSuperclassTypeParameter(Class<?> subclass) {
        System.out.println("subclass = " + subclass.toString());
        Type superclass = subclass.getGenericSuperclass();
        System.out.println("superclass = " + subclass.toString());
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Test
    public void testClass(){
        TestResult result = new TestResult();
        OnSuccessCallback<TestResult> callback = new OnSuccessCallback<TestResult>() {
            @Override
            public void success(TestResult result) {
                System.out.println(result.toString());
            }
        };
        callback.getClass().getGenericInterfaces();
        Type[] types = callback.getClass().getGenericInterfaces();
        if (types.length > 0){
            Type type = types[0];
            if (type instanceof ParameterizedType){
                System.out.println("yes");
                ParameterizedType pType = (ParameterizedType)type;
                System.out.println(pType.getRawType().toString());

                //取得泛型类型的泛型参数
                Type[] tArgs = pType.getActualTypeArguments();
                if (tArgs.length > 0){
                    System.out.println("有一个" + tArgs.length);
                    Type realType = tArgs[0];
                    String typeStr = realType.toString();
                    String[] strings = typeStr.split(" ");
                    System.out.println(strings[1]);
                }
            }
            System.out.println(type.toString());

            try {
                Class clazz =  Class.forName("com.liwy.easyhttp.test.TestResult");
                System.out.println(clazz.getSimpleName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}