package com.githang.dinny;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-28
 */
abstract class ExtraHandler {

    private static final HashMap<Type, Method> paramMethods = new HashMap<>();

    final String name;
    final Method method;

    private ExtraHandler(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    abstract void apply(final Intent intent, Object extra) throws InvocationTargetException, IllegalAccessException;

    static ExtraHandler find(String name, Type type) {
        Method method = paramMethods.get(type);
        if (method == null) {
            try {
                method = getMethod(name, type);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
            if (method == null) {
                return null;
            }
            paramMethods.put(type, method);
        }
        if (name.isEmpty()) {
            return new ExtraHandler.PutExtras(name, method);
        } else {
            return new ExtraHandler.PutExtra(name, method);
        }
    }

    private static Method getMethod(String name, Type type) throws NoSuchMethodException {
        final Class rawType = Utils.getRawType(type);
        if (type instanceof ParameterizedType) {
            if (!ArrayList.class.isAssignableFrom((Class<?>) rawType)) {
                return null;
            }
            final Class paramType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            if (paramType == Integer.class) {
                return getIntentMethod("putIntegerArrayListExtra", String.class, ArrayList.class);
            } else if (paramType == String.class) {
                return getIntentMethod("putStringArrayListExtra", String.class, ArrayList.class);
            } else if (CharSequence.class.isAssignableFrom(paramType)) {
                return getIntentMethod("putCharSequenceArrayListExtra", String.class, ArrayList.class);
            } else if (Parcelable.class.isAssignableFrom(paramType)) {
                return getIntentMethod("putParcelableArrayListExtra", String.class, ArrayList.class);
            } else {
                return null;
            }
        } else {
            if (name.isEmpty()) {
                if (rawType == Intent.class || rawType == Bundle.class) {
                    return getIntentMethod("putExtras", rawType);
                } else {
                    return null;
                }
            }
            if (rawType.isPrimitive()) {
                return getPutExtraMethod(rawType);
            } else if (rawType == Boolean.class) {
                return getPutExtraMethod(boolean.class);
            } else if (rawType == Byte.class) {
                return getPutExtraMethod(byte.class);
            } else if (rawType == Character.class) {
                return getPutExtraMethod(char.class);
            } else if (rawType == Short.class) {
                return getPutExtraMethod(short.class);
            } else if (rawType == Integer.class) {
                return getPutExtraMethod(int.class);
            } else if (rawType == Long.class) {
                return getPutExtraMethod(long.class);
            } else if (rawType == Float.class) {
                return getPutExtraMethod(float.class);
            } else if (rawType == Double.class) {
                return getPutExtraMethod(double.class);
            } else if (rawType == String.class || rawType == Bundle.class || rawType.isArray()) {
                return getPutExtraMethod(rawType);
            } else if (CharSequence.class.isAssignableFrom(rawType)) {
                return getPutExtraMethod(CharSequence.class);
            } else if (Parcelable.class.isAssignableFrom(rawType)) {
                return getPutExtraMethod(Parcelable.class);
            } else if (Serializable.class.isAssignableFrom(rawType)) {
                return getPutExtraMethod(Serializable.class);
            } else {
                return null;
            }
        }
    }


    private static Method getPutExtraMethod(Class paramType) throws NoSuchMethodException {
        return getIntentMethod("putExtra", String.class, paramType);
    }

    private static Method getIntentMethod(String methodName, Class... types) throws NoSuchMethodException {
        return Intent.class.getDeclaredMethod(methodName, types);
    }

    static class PutExtra extends ExtraHandler {

        private PutExtra(String name, Method method) {
            super(name, method);
        }

        @Override
        void apply(final Intent intent, Object extra) throws InvocationTargetException, IllegalAccessException {
            method.invoke(intent, name, extra);
        }
    }

    static class PutExtras extends ExtraHandler {

        private PutExtras(String name, Method method) {
            super(name, method);
        }

        @Override
        void apply(Intent intent, Object extra) throws InvocationTargetException, IllegalAccessException {
            method.invoke(intent, extra);
        }
    }
}
