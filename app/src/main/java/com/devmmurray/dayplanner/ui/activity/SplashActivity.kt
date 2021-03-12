package com.devmmurray.dayplanner.ui.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import com.devmmurray.dayplanner.util.notifications.AlarmReceiver
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import java.util.*

private const val REQ_CODE_PERMISSION = 123
private const val TAG = "Splash Activity"

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashActivityViewModel by viewModels()
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        checkLocationPermission()
        setAlarm()

        splashViewModel.apply {
            errorMessage.observe(this@SplashActivity, errorObserver)
            ioException.observe(this@SplashActivity, ioExceptionObserver)
            databaseNotReady.observe(this@SplashActivity, databaseObserver)
        }
    }

    private fun startApp() {
        splashViewModel.deleteOldData()
        splashViewModel.getNewData(location)
        location?.let { getCityState(it) }
    }

    private fun getCityState(location: Location) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        splashViewModel.getCityState(addresses)
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


    /**
     *  Setting up alarm for daily notification
     */

    private fun setAlarm() {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
        }

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        val pending = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pending
        )
    }

}


