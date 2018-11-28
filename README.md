Dinny
===

Dinny是一个处理Android中Activity跳转的库。

# Dinny 能做什么

假如我们所要跳转的TestActivity需要有相关的数据，以前我们可能是在调用的地方写下如下代码：

```kotlin
    val intent = Intent(this, TestActivity::class.java)
            .putExtra("boolean", isNew) // isNew为boolean类型
            .putExtra("data", user) // 假设User类实现了Parcelable接口
            .putExtra("number", number) // number为String类型
    startActivity(intent)
```

如果有多处地方跳转到这个界面，可能就需要在多处地方编写如上代码。当然，我们也可以将其抽出成为静态方式。但是将需要传数据的界面较多时，同样会面临重复代码较多的问题。

而使用Dinny，你可以如下实现：

将一个模块下的所有Activity跳转声明到一个接口文件中，如：

ActivityProtocol.kt

```kotlin
interface ActivityProtocol {
    @ToActivity(TestActivity::class)
    fun startTest(context: Context, @Extra("boolean") isNew: Boolean, @Extra("data") user: User, @Extra("number") number: String)
    
    // ...
}
```

然后在Activity中就可以如下调用：

```kotlin
    Dinny.create(ActivityProtocol::class.java).startTest(this, isNew, user, number)
```

如果使用kotlin的一些语法特性，代码还可以更简洁。如下：
先声明一个object：

```kotlin
object ActivityCtrl : ActivityProtocol by Dinny.create(ActivityProtocol::class.java)
```

然后前面调用的代码就可以简化为：

```kotlin
    ActivityCtrl.startTest(this, isNew, user, number)
```

如果我们不想要直接跳转，而是要返回一个Intent，来进行startActivityForResult或者添加一个Flag，那么声明接口时可以添加返回类型Intent，如下：

```kotlin
interface ActivityProtocol {
    @ToActivity(UserListActivity::class)
    fun chooseUserIntent(context: Context, @Extra("data") current: User): Intent
    
    // ...
}
```

# Dinny 的思想

从其他界面跳转到一个界面来，需要传什么数据，可以看作是向这个界面跳转的一种协议。而将这些协议声明为接口里的方法，也就是把它们协议化，一可以简化代码，二可以提高代码阅读性，三则可以把一个功能模块下的所有界面跳转都进行集中管理，便于维护。
而这个接口可以认为是这个模块下的界面向外开放的协议，它是这个模块内的界面所期望的跳转方式。
