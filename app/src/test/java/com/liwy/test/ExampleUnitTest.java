package com.liwy.test;

import com.liwy.test.bean.TestResult;
import com.liwy.test.callback.SuccessCallback;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
    public void testClass(){
        TestResult result = new TestResult();
        SuccessCallback<TestResult> callback = new SuccessCallback<TestResult>() {
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