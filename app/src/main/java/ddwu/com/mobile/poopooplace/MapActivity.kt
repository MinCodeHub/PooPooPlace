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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    var exact_location: String? = null   //geocode한 화장실의 구체적인 위치 정보를 담을 변수 -> 마커 snippet
    var latitude: String? = null  //intent로 받아올 위도 값을 저장할 변수
    var longitude: String? = null //intent로 받아올 경도 값을 저장할 변수
    var restroomlocation: String? = null  //api에 있는 대명칭인 장소 건물 이름 intent로 받아올 값을 담을 변수 -> 마커 title
    var centerMarker: Marker? = null //마커 변수
    var restroomtype:String? = null  //민간 화장실인지 개방 화장실인지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mapBinding.root)

        latitude = intent.getStringExtra("위도")
        longitude = intent.getStringExtra("경도")
        restroomlocation = intent.getStringExtra("화장실")
        restroomtype = intent.getStringExtra("화장실 타입")
        
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
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onMapReady(map: GoogleMap) {
            googleMap = map //map 정보 가져오기 완료 시 멤버변수에 저장
            var dlat = latitude?.toDouble()
            var dlogi = longitude?.toDouble()

            if (dlat != null && dlogi != null) {
                //위도 경도 위치 설정
                val targetLocation = LatLng(dlat, dlogi)

                //지오코딩
                geocoder.getFromLocation(dlat, dlogi, 5) { addresses ->
                    CoroutineScope(Dispatchers.Main).launch {
                        exact_location = addresses.get(0).getAddressLine(0).toString()
                        //showData(restroomtype.toString())
                        showData(addresses.get(0).getAddressLine(0).toString())
                        //카메라 움직이기
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                targetLocation,
                                17F
                            )
                        )
                        addMarker(targetLocation, addresses.get(0).getAddressLine(0).toString())
                    }
                }
            }
        }
    }

    fun addMarker(targetLoc: LatLng, exactLocation: String?) {
        Log.d(TAG, "exact_location : ${exactLocation}")
        val markerOptions: MarkerOptions = MarkerOptions() // 마커를 표현하는 Option 생성
        markerOptions.position(targetLoc) // 필수
            .title(restroomlocation)
            .snippet(exactLocation ?: "Location information not available")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        centerMarker = googleMap.addMarker(markerOptions) // 지도에 마커 추가, 추가마커 반환
        centerMarker?.showInfoWindow() // 마커 터치 시 InfoWindow 표시
//        centerMarker?.tag = "database_id"
        // 마커에 관련 정보(Object) 저장


        // 마커 클릭 이벤트 처리
        googleMap.setOnMarkerClickListener { marker ->
            Toast.makeText(this, marker.tag.toString(), Toast.LENGTH_SHORT).show()
            false // true일 경우 이벤트처리 종료이므로 info window 미출력
        }

        // 마커 InfoWindow 클릭 이벤트 처리
        googleMap.setOnInfoWindowClickListener { marker ->
            Toast.makeText(this, marker.title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showData(data: String) {
        mapBinding.tvData.setText(mapBinding.tvData.text.toString() + "\n${data}")
    }
}