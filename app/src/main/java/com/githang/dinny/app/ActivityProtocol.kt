package com.githang.dinny.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.githang.dinny.annotations.Extra
import com.githang.dinny.annotations.ToActivity
import java.io.Serializable

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2018-11-27
 */
interface ActivityProtocol {
    @ToActivity(TestActivity::class)
    fun newTestIntent(context: Context): Intent

    // would be failed because Intent.putExtra method doesn't support HashMap type.
    @ToActivity(TestActivity::class)
    fun newTestIntent(context: Context, @Extra("map") map: HashMap<String, String>): Intent

    @ToActivity(TestActivity::class)
    fun newTestIntent(
        context: Context,
        @Extra("CharSequenceArrayList") charSequenceArrayList: ArrayList<CharSequence>,
        @Extra("Parcelable") parcelable: Parcelable,
        @Extra("longArray") longArray: LongArray,
        @Extra("byte") byte: Byte,
        @Extra("doubleArray") doubleArray: DoubleArray,
        @Extra("CharSequence") charSequence: CharSequence,
        @Extra("booleanArray") booleanArray: BooleanArray,
        @Extra("int") int: Int,
        @Extra("charArray") charArray: CharArray,
        @Extra("byteArray") byteArray: ByteArray,
        @Extra("ParcelableArray") parcelableArray: Array<Parcelable>,
        @Extra("Bundle") bundle: Bundle?,
        @Extra("CharSequenceArray") charSequenceArray: Array<CharSequence>,
        @Extra("floatArray") floatArray: FloatArray,
        @Extra("double") double: Double,
        @Extra("intArray") intArray: IntArray,
        @Extra("stringArray") stringArray: Array<String>,
        @Extra("shortArray") shortArray: ShortArray,
        @Extra("boolean") boolean: Boolean,
        @Extra("String") string: String,
        @Extra("long") long: Long,
        @Extra("char") char: Char,
        @Extra("Serializable") serializable: Serializable,
        @Extra("float") float: Float,
        @Extra("short") short: Short,
        @Extra intent: Intent?,
        @Extra extras: Bundle?
    ): Intent

    @ToActivity(TestActivity::class)
    fun startTestActivity(activity: Activity)
}