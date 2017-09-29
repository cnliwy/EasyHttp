package com.liwy.easyhttp;

import com.liwy.easyhttp.callback.OnSuccessCallback;

import org.junit.Test;

import java.util.List;

import okhttp3.ResponseBody;

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
    public void testType(){
        OnSuccessCallback<List<ResponseBody>> onSuccessCallback = new OnSuccessCallback<List<ResponseBody>>() {
            @Override
            public void success(List<ResponseBody> result) {

            }
        };
        System.out.println(onSuccessCallback.rawType);
        System.out.println(onSuccessCallback.mType);
    }
}