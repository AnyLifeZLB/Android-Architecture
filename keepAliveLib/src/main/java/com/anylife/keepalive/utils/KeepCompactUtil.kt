package com.anylife.keepalive.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log

/**
 * 原生跳转引导
 *
 * @author zenglb@vanke.com
 */
object KeepCompactUtil {

    private val AUTO_START_INTENTS = arrayOf(

        // 小米
        Intent().setComponent(
            ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
        ),

        // 华为
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity")
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity")
        ),

        // 魅族
        Intent().setComponent(ComponentName.unflattenFromString("com.meizu.safe/.SecurityCenterActivity")),

        // 三星
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.autorun.ui.AutoRunActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.ram.AutoRunActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.appmanagement.AppManagementActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.autorun.ui.AutoRunActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.ram.AutoRunActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.appmanagement.AppManagementActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity"
            )
        ),
        Intent().setComponent(
            ComponentName.unflattenFromString(
                "com.samsung.android.sm_cn/.app.dashboard.SmartManagerDashBoardActivity"
            )
        ),
        Intent().setComponent(
            ComponentName.unflattenFromString(
                "com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity"
            )
        ),

        // oppo
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.coloros.safecenter/.startupapp.StartupAppListActivity")
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.coloros.safecenter/.permission.startupapp.StartupAppListActivity")
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.safecenter",
                "com.coloros.privacypermissionsentry.PermissionTopActivity"
            )
        ),
        Intent().setComponent(
            ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity")
        ),

        // vivo
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.vivo.permissionmanager/.activity.BgStartUpManagerActivity")
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.iqoo.secure/.phoneoptimize.BgStartUpManager")
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.vivo.permissionmanager/.activity.PurviewTabActivity")
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.iqoo.secure/.ui.phoneoptimize.SoftwareManagerActivity")
        ),

        // 一加
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.oneplus.security/.chainlaunch.view.ChainLaunchAppListActivity")
        ),

        // 乐视
        Intent().setComponent(
            ComponentName.unflattenFromString("com.letv.android.letvsafe/.AutobootManageActivity")
        ),

        // HTC
        Intent().setComponent(
            ComponentName.unflattenFromString("com.htc.pitroad/.landingpage.activity.LandingPageActivity")
        )
    )

    private val BATTERY_INTENTS = arrayOf(
        // 小米
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.miui.powerkeeper/.ui.HiddenAppsContainerManagementActivity")
        ),


        // 华为， 前后各种不一致，混乱
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.power.ui.HwPowerManagerActivity")
        ),

        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity")
        ),

        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.optimize.bootstart.BootStartActivity")
        ),

        //华为鸿蒙OS 3，P20，
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.huawei.systemmanager/.mainscreen.MainScreenActivity")
        ),


        // 魅族
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.meizu.safe/.SecurityCenterActivity")
        ),

        // 三星
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.battery.AppSleepListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.battery.BatteryActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.battery.AppSleepListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.battery.BatteryActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.lool",
                "com.samsung.android.sm.battery.ui.BatteryActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.lool",
                "com.samsung.android.sm.ui.battery.BatteryActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm",
                "com.samsung.android.sm.ui.battery.BatteryActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity"
            )
        ),

        // oppo
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.coloros.safecenter/.appfrozen.activity.AppFrozenSettingsActivity")
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.oppoguardelf",
                "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.oppoguardelf",
                "com.coloros.powermanager.fuelgaue.PowerSaverModeActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.oppoguardelf",
                "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity"
            )
        ),
        Intent().setComponent(
            ComponentName
                .unflattenFromString("com.oppo.safe/.SecureSafeMainActivity")
        ),

        // vivo
        Intent().setComponent(
            ComponentName(
                "com.vivo.abe",
                "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"
            )
        ),
        Intent().setComponent(ComponentName.unflattenFromString("com.iqoo.powersaving/.PowerSavingManagerActivity"))
    )


    var phoneBrandEnumLists: List<PhoneBrandEnum> = object : ArrayList<PhoneBrandEnum>() {
        init {
            add(PhoneBrandEnum.Huawei)
            add(PhoneBrandEnum.Xiaomi)
            add(PhoneBrandEnum.Oppo)
            add(PhoneBrandEnum.Vivo)
            add(PhoneBrandEnum.Samsung)
            add(PhoneBrandEnum.Honor)
            add(PhoneBrandEnum.NONE)
        }
    }

    //区分出华为和Honor 吧，他们不会再是一家了吧
    enum class PhoneBrandEnum {
        Huawei, Xiaomi, Oppo, Vivo, Samsung, Honor, Yijia, NONE
    }


    /**
     * adb shell dumpsys activity top
     *
     * @return 是否为三星s9 型号的手机
     */
    val isSamsungS9: Boolean
        get() = ("samsung".equals(Build.BRAND, ignoreCase = true) && !TextUtils.isEmpty(Build.MODEL)
                && Build.MODEL.startsWith("SM-G9"))


    /**
     * 获取型号
     *
     */
    val deviceEnum: PhoneBrandEnum
        get() {
            //华为，荣耀分开，虽然底层还是荣耀的字样

            if ("Huawei".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Huawei
            }


            if ("vivo".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Vivo
            }

            if ("OPPO".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Oppo
            }
            if ("Xiaomi".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Xiaomi
            }
            if ("Honor".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Honor
            }
            if ("samsung".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Samsung
            }

            if ("oneplus".equals(Build.BRAND, ignoreCase = true)) {
                return PhoneBrandEnum.Yijia
            }

            return if ("ZTE".equals(Build.BRAND, ignoreCase = true)) {
                PhoneBrandEnum.NONE
            } else PhoneBrandEnum.NONE

        }

    // 进程守护, 自启动
    fun daemonSet(activity: Activity): Boolean {
        for (intent in AUTO_START_INTENTS) {
            if (activity.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            ) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    activity.startActivity(intent)
                    return true
                } catch (e: Exception) {
                    Log.e("KeepCompactUtil", e.toString())
                    continue
                }
            }
        }
        return false
    }

    // 防睡眠,电池管理
    fun noSleepSet(activity: Activity): Boolean {
        for (intent in BATTERY_INTENTS) {
            if (activity.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            ) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    activity.startActivity(intent)
                    return true
                } catch (e: Exception) {
                    Log.e("KeepCompactUtil", e.toString())
                    continue
                }
            }
        }
        return false
    }
}