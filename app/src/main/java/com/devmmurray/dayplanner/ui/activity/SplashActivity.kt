package com.devmmurray.dayplanner.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.ui.viewmodel.SplashActivityViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor

private const val REQ_CODE_PERMISSION = 123

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashActivityViewModel by viewModels()
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkLocationPermissions()
        splashViewModel.addLocation(location)
        splashViewModel.deleteOldWeatherData()


        splashViewModel.apply {
            errorMessage.observe(this@SplashActivity, errorObserver)
            databaseReady.observe(this@SplashActivity, databaseObserver)
        }
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

    private val databaseObserver = Observer<Boolean> {
        if (!it) {
            startActivity(intentFor<MainActivity>())
        }
    }

    /**
     *  Permission Request for Location Services
     */

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
                }
                return
            }
        }

    }

    private fun checkLocationPermissions() {
        val locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQ_CODE_PERMISSION
            )
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }
}