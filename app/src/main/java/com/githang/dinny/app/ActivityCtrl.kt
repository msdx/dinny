package com.githang.dinny.app

import com.githang.dinny.Dinny

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-27
 */
object ActivityCtrl : ActivityProtocol by Dinny.create(ActivityProtocol::class.java)
