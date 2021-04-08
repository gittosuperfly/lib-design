# lib-design
用于简单Drawable的代码生成，减少shape.xml的使用

## 引入

[![](https://jitpack.io/v/gittosuperfly/lib-design.svg)](https://jitpack.io/#gittosuperfly/lib-design)


**Step 1**. 添加JitPack repository到你项目的build.gradle文件

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2**. 添加库依赖
```groovy
	dependencies {
	    implementation 'com.github.gittosuperfly:lib-design:Version'
	}
```


## 使用

**Step 1**. 在Application中初始化Design：

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //配置对象
        val config = Design.Config(this)
        //配置举例
        config.colorConfig = Design.ColorConfig(0.4f, 0.4f)
        config.iconDrawableConfig = Design.IconDrawableConfig(pressedAlpha = 0.4f)
        //应用配置
        Design.init(config)
        //或者直接使用默认配置
        Design.init(this)
    }
}
```

**Step 2**. 在代码中弹出Toast

```kotlin
fun testBtnClick(){
    button.setOnClickListener {
        //生成一个粉色、带圆角的Drawable
        background = Design.rectDrawable(Color.parseColor("#ff4090"), 20f).build()
        //Tips:更多用法详见API
    }
}
```



## 功能点

* 可设置点击、选中状态
* 设置渐变背景色
* 绘制圆角矩形、椭圆形等

