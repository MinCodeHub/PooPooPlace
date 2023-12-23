package ddwu.com.mobile.poopooplace

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.poopooplace.databinding.ActivityMapBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


private lateinit var googleMap: GoogleMap

class MapActivity : AppCompatActivity() {
    var TAG = "g"
    val mapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var currentLoc: Location
    private lateinit var googleMap: GoogleMap
    var latitude: String? = null
    var longitude: String? = null

//    var centerMarker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mapBinding.root)

        latitude = intent.getStringExtra("위도")
        longitude = intent.getStringExtra("경도")

        Log.d(TAG, "latitude is ${latitude}")
        Log.d(TAG, "longitude is ${longitude}")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())


        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)


    }

    //map 정보 가져 오기 완료 확인 Callback
    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map //map 정보 가져오기 완료 시 멤버변수에 저장
            val dlat = latitude?.toDouble()
            val dlogi = longitude?.toDouble()

            if (dlat != null && dlogi != null) {
                //위도 경도 위치 설정
                val targetLocation = LatLng(dlat,dlogi)

                //카메라 움직이기
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 17F))
                Log.d(TAG, "GoogleMap is ready")
                Log.d(TAG, "Actual latitude: $dlat, Actual longitude: $dlogi")


            }
        }
    }
}