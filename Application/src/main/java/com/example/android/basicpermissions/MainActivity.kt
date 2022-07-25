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
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.basicpermissions.camera.CameraPreviewActivity
import com.example.android.basicpermissions.util.requestPermissionsCompat

const val PERMISSION_REQUEST_CAMERA = 0
const val MESSAGE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA =
    "Sorry, but we can't open this function without your permission to camera \uD83D\uDE44 \n" +
            "Press \"OK\" for add access by yourself"
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

        // Register a listener for the 'Show Camera Preview' button.
        findViewById<Button>(R.id.button_open_camera).setOnClickListener {
            requestCameraPermissions()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PackageManager.PERMISSION_GRANTED -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    startCamera()
                } else {
                    repeatMessageAccessCamera()
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {

            }
        }
    }

    private fun startCamera() {

        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun requestCameraPermissions() {
        requestPermissionsCompat(
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CAMERA
        )
    }

    private fun showPermRationale() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", packageName, null
        )
        intent.data = uri
        startActivity(intent)
    }

    private fun repeatMessageAccessCamera() {
        AlertDialog.Builder(this)
            .setTitle(TITLE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA)
            .setMessage(MESSAGE_FOR_USER_ABOUT_PERMISSION_TO_CAMERA)
            .setPositiveButton("OK")
            { _, _ ->
                showPermRationale()
            }.setNegativeButton("Cancel")
            { _,_ ->
                onBackPressedDispatcher
            }.show()
    }
}

