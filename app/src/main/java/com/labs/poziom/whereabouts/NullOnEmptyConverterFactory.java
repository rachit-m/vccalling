package com.labs.poziom.whereabouts;

import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sandy on 16-01-2017.
 */

public class NullOnEmptyConverterFactory extends Converter.Factory {

    private final GsonConverterFactory mGsonConverterFactory;

    public NullOnEmptyConverterFactory(GsonConverterFactory gsonConverterFactory) {
        mGsonConverterFactory = gsonConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return mGsonConverterFactory.requestBodyConverter(type, parameterAnnotations,
                methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegateConverter =
                mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
        return new Converter<ResponseBody, Object>() {

            @Override
            public Object convert(ResponseBody value) throws IOException {

                if (value.contentLength() == 0) return null;
                return delegateConverter.convert(value);
            }
        };
    }
}