package com.architecture.demo.ui.navi.alitest

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.architecture.demo.R

class AliActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ali)

        val message=findViewById<TextView>(R.id.message)


        val aliViewModel =
            ViewModelProvider(this)[AliViewModel::class.java]

        message.setOnClickListener{
            aliViewModel.requestNet()
        }



        //把请求的数据格式返回了就可以了
        aliViewModel.fakeDataList.observe(this) {
            message.text = it.toString()
        }

    }
}