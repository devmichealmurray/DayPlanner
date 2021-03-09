package com.devmmurray.dayplanner.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.ui.viewmodel.SplashActivityViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import java.util.*

private const val REQ_CODE_PERMISSION = 123

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashActivityViewModel by viewModels()
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        checkLocationPermission()

        splashViewModel.apply {
            errorMessage.observe(this@SplashActivity, errorObserver)
            ioException.observe(this@SplashActivity, ioExceptionObserver)
            databaseNotReady.observe(this@SplashActivity, databaseObserver)
        }
    }

    private fun startApp() {
        splashViewModel.deleteOldData()
        splashViewModel.getNewData(location)
    }


    /**
     *  Live Data Observers
     */

    private val errorObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_dialog)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private val ioExceptionObserver = Observer<Boolean> {
        if (it) {
            AlertDialog.Builder(this)
                .setTitle("Internet Connection")
                .setMessage("UH OH!\nNo Network Connection Found.\nWould you like to adjust your wifi settings?")
                .setPositiveButton("Yes!") { _: DialogInterface, _: Int ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }
                .show()
        }
    }

    private val databaseObserver = Observer<Boolean> {
        if (!it) {
            startActivity(intentFor<MainActivity>())
        }
    }


    /**
     *  Permission Request for Location Services
     */

    private fun checkLocationPermission() {
        val locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            startApp()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Location Permission Needed For Local Weather, Local Time, and Driving Directions")
                .setPositiveButton("Okay") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        REQ_CODE_PERMISSION
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQ_CODE_PERMISSION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        when (requestCode) {
            REQ_CODE_PERMISSION -> {
                if ((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    startApp()
                } else {
                    Toast.makeText(
                        this,
                        "Permission DENIED: Cannot Start Application",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        return
    }


}


