/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.basicpermissions

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.basicpermissions.camera.CameraPreviewActivity
import com.example.android.basicpermissions.util.checkSelfPermissionCompat
import com.example.android.basicpermissions.util.requestPermissionsCompat

const val PERMISSION_REQUEST_CAMERA = 0
const val MESSAGE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA =
    "Якщо ви не надасте доступ до камери, то Іванич не зарахує мені домашку.. \uD83D\uDE44"
const val TITLE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA = "Доступ до камери"

/**
 * Launcher Activity that demonstrates the use of runtime permissions for Android M.
 * This Activity requests permissions to access the camera
 * ([android.Manifest.permission.CAMERA])
 * when the 'Show Camera Preview' button is clicked to start  [CameraPreviewActivity] once
 * the permission has been granted.
 *
 * <p>First, the status of the Camera permission is checked using [ActivityCompat.checkSelfPermission]
 * If it has not been granted ([PackageManager.PERMISSION_GRANTED]), it is requested by
 * calling [ActivityCompat.requestPermissions]. The result of the request is
 * returned to the
 * [android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback], which starts
 * if the permission has been granted.
 *
 * <p>Note that there is no need to check the API level, the support library
 * already takes care of this. Similar helper methods for permissions are also available in
 * ([ActivityCompat],
 * [android.support.v4.content.ContextCompat] and [android.support.v4.app.Fragment]).
 */
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)

        // Call the request to camera
        requestCameraPermissions()

        findViewById<Button>(R.id.button_open_camera).setOnClickListener {
            val checkPermissionStatus = checkSelfPermissionCompat(Manifest.permission.CAMERA)

            if (checkPermissionStatus == PackageManager.PERMISSION_GRANTED) {
                showCamera()
            }

            if (checkPermissionStatus == PackageManager.PERMISSION_DENIED) {
                repeatMessageAccessCamera()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showCamera() {
        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun requestCameraPermissions() {
        return requestPermissionsCompat(
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CAMERA
        )
    }

    private fun repeatMessageAccessCamera() {
        AlertDialog.Builder(this)
            .setTitle(TITLE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA)
            .setMessage(MESSAGE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA)
            .setPositiveButton(R.string.Ok)
            { _, _ ->
                requestCameraPermissions()
            }.show()
    }

}



