package com.architecture.demo.ui.archi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anylife.keepalive.keepalive.DozeAliveService
import com.anylife.keepalive.utils.DozeServiceUtils
import com.architecture.demo.R
import com.architecture.demo.databinding.ActivityDemoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_demo)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //这里启动Notification,用于保活---不一定可用，多少有点用
        if (!DozeServiceUtils.isServiceRunning(DozeAliveService::class.java.toString(),baseContext)) {
            //支持传参数
            ContextCompat.startForegroundService(this, Intent(this, DozeAliveService::class.java))
        }


    }
}