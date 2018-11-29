package com.githang.dinny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.githang.dinny.annotations.Extra;
import com.githang.dinny.annotations.ToActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-27
 */
class ProtocolMethod {
    private Class<? extends Activity> target;
    private ExtraHandler[] extraHandlers;
    private boolean start;

    private ProtocolMethod(Class<? extends Activity> target, ExtraHandler[] extraHandlers, boolean start) {
        this.target = target;
        this.extraHandlers = extraHandlers;
        this.start = start;
    }

    Intent invoke(Object... args) {
        final Context from = (Context) args[0];
        final Intent intent = new Intent(from, target);
        try {
            for (int i = 1; i < extraHandlers.length; i++) {
                final Object arg = args[i];
                if (arg != null) {
                    extraHandlers[i - 1].apply(intent, arg);
                }
            }
        } catch (Exception e) {
            Log.e("Dinny", "put extra error ", e);
        }
        if (start) {
            from.startActivity(intent);
            return null;
        } else {
            return intent;
        }
    }

    static class Builder {
        private final Method method;
        private final Annotation[] methodAnnotations;
        private final Type[] paramTypes;
        private final Annotation[][] paramAnnotations;
        private final Class<?> returnType;

        Builder(Method method) {
            this.method = method;
            methodAnnotations = method.getAnnotations();
            paramTypes = method.getGenericParameterTypes();
            paramAnnotations = method.getParameterAnnotations();
            returnType = method.getReturnType();
        }

        ProtocolMethod build() {
            Class<? extends Activity> target = null;
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof ToActivity) {
                    target = ((ToActivity) annotation).value();
                }
            }
            if (target == null) {
                throw methodError("@ToActivity method annotation is required.");
            }

            checkFirstParam();

            final int paramCount = paramTypes.length;
            final ExtraHandler[] paramHandlers = new ExtraHandler[paramCount - 1];
            for (int p = 1; p < paramCount; p++) {
                final Type type = paramTypes[p];
                final Annotation[] annotations = paramAnnotations[p];
                paramHandlers[p - 1] = parseParam(p, type, annotations);
            }

            boolean start = false;
            if (returnType == void.class) {
                start = true;
            } else if (!Intent.class.isAssignableFrom(returnType)) {
                throw methodError("The returnType must be Intent");
            }

            return new ProtocolMethod(target, paramHandlers, start);
        }

        private void checkFirstParam() {
            final Type type = paramTypes[0];
            if (!(type instanceof Class) || !Context.class.isAssignableFrom((Class<?>) type)) {
                throw methodError("The first parameter type must be Context or it's subclass.");
            }
        }

        private ExtraHandler parseParam(int p, Type type, Annotation[] annotations) {
            if (annotations == null) {
                throw paramError(p, "No annotation found.");
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof Extra) {
                    final ExtraHandler handler = ExtraHandler.find(((Extra) annotation).value(), type);
                    if (handler == null) {
                        throw paramError(p, "The parameter type [%s] is unsupported", type.toString());
                    }
                    return handler;
                }
            }
            throw paramError(p, "No Dinny annotation found.");
        }

        private IllegalArgumentException methodError(String message, Object... args) {
            return new IllegalArgumentException(
                    String.format(message, args)
                            + "\n    for method " + method.getDeclaringClass().getSimpleName() + "." + method.getName()
            );
        }

        private IllegalArgumentException paramError(int p, String message, Object... args) {
            return methodError(message + " (parameter #" + (p + 1) + ")", args);
        }
    }
}