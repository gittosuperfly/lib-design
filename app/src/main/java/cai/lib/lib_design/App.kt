package cai.lib.lib_design

import android.app.Application
import cai.lib.design.Design

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
        //Design.init(this)
    }
}