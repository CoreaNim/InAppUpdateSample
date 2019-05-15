package com.gasgasstation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity() {

    lateinit var manager: AppUpdateManager
    val REQUEST_CODE = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = AppUpdateManagerFactory.create(this)
        manager.appUpdateInfo.addOnCompleteListener { task ->
            val info = task.result

            when (info.updateAvailability()) {
                UpdateAvailability.UPDATE_AVAILABLE -> {
                    // 업데이트 작업 수행
                    manager.startUpdateFlowForResult(info, AppUpdateType.IMMEDIATE, this, REQUEST_CODE)
                }
                UpdateAvailability.UPDATE_NOT_AVAILABLE -> {
                    // 업데이트가 없는 경우
                    Toast.makeText(this, "최신 버전입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        manager.appUpdateInfo.addOnCompleteListener { task ->
            val appUpdateInfo = task.result
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                manager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE)
            }
        }
    }
}
