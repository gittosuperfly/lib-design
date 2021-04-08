package cai.lib.lib_design

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import cai.lib.design.Design

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.helloTv).apply {
            background = Design.rectDrawable(Color.parseColor("#ff4090"), 20f).build()
        }
    }
}