package com.githang.dinny.annotations;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ToActivity {
    Class<? extends Activity> value();
}