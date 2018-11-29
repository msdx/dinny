package com.githang.dinny;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-27
 */
public class Dinny {

    private static final Map<Method, ProtocolMethod> methodCache = new LinkedHashMap<>();

    private Dinny() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> protocol) {
        Utils.validateProtocolInterface(protocol);
        return (T) Proxy.newProxyInstance(protocol.getClassLoader(), new Class[]{protocol}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                final ProtocolMethod protocolMethod = loadProtocolMethod(method);
                return protocolMethod.invoke(args);
            }
        });
    }

    private static ProtocolMethod loadProtocolMethod(Method method) {
        ProtocolMethod result = methodCache.get(method);
        if (result != null) {
            return result;
        }
        synchronized (methodCache) {
            result = methodCache.get(method);
            if (result == null) {
                result = new ProtocolMethod.Builder(method).build();
                methodCache.put(method, result);
            }
        }
        return result;
    }
}