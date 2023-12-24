package ddwu.com.mobile.poopooplace.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
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
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.databinding.FragmentNearbyToiletsBinding
import ddwu.com.mobile.poopooplace.databinding.FragmentSearchToiletsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

class NearbyToiletsFragment : Fragment() {
    //내 주변 화장실 찾기
    //없으면 없다고 알려 주고 있으면 마커 찍어주기
    private val TAG = "MActivity"
    private var mBinding: FragmentNearbyToiletsBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var googleMap: GoogleMap
    var exact_location: String? = null   //geocode한 화장실의 구체적인 위치 정보를 담을 변수 -> 마커 snippet
    var latitude: Double? = null  //intent로 받아올 위도 값을 저장할 변수
    var longitude: Double? = null //intent로 받아올 경도 값을 저장할 변수
    var centerMarker: Marker? = null //마커 변수
//    private lateinit var currentLoc: Location

    private var isLocationDataReady = false //위치 데이터가 준비되었는지 추적하는 플래그

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binging = FragmentNearbyToiletsBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        mBinding = binging

        mBinding?.btnPermit?.setOnClickListener {
            checkPermissions()
        }

        mBinding?.btnLastLoc?.setOnClickListener {
            getLastLocation()

            // callExternalMap()
        }


        mBinding?.btnLocStart?.setOnClickListener {
            startLocUpdates()
        }

        mBinding?.btnLocStop?.setOnClickListener {

        }

        mBinding?.btnLocTitle?.setOnClickListener {
            getLastLocation()
            //callExternalMap()
        }



        return mBinding?.root
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locCallback)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun checkPermissions() {
        if (checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            showData("Permissions are already granted")  // textView에 출력
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /*registerForActivityResult 는 startActivityForResult() 대체*/
    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    showData("FINE_LOCATION is granted")
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    showData("COARSE_LOCATION is granted")
                }

                else -> {
                    showData("Location permissions are required")
                }
            }
        }


    val locCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locResult: LocationResult) {
            val currentLoc: Location = locResult.locations[0]
            //showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
            latitude = currentLoc.latitude  //변수에 위도 경도 저장
            longitude = currentLoc.longitude
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    currentLoc.latitude,
                    currentLoc.longitude,
                    1
                ) { addresses ->
                    CoroutineScope(Dispatchers.Main).launch {
                        showData(addresses.get(0).getAddressLine(0).toString())
                        isLocationDataReady = true  //위치 데이터 준비 완료
                        handleLocationDataReady()  //handleLocationDataReady() 실행

                    }

                }
            }
        }

    }
    val locRequest = LocationRequest.Builder(10000)
        .setMinUpdateIntervalMillis(5000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()


    private fun startLocUpdates() {
        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locRequest,
            locCallback,
            Looper.getMainLooper()
        )
        isLocationDataReady = false //위치 데이터 준비 안됨

    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                showData(location.toString())
            }
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, e.toString())
        }
    }


    //    fun callExternalMap() {
//        val locLatLng   // 위도/경도 정보로 지도 요청 시
//                = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
//        val locName     // 위치명으로 지도 요청 시
//                = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
//        val route       // 출발-도착 정보 요청 시
//                = String.format(
//            "https://www.google.co.kr/maps?saddr=%f,%f&daddr=%f,%f",
//            37.606320, 127.041808, 37.601925, 127.041530
//        )
//        //위도 경도 결합 우리나라에서는 잘 안됨
//        //필요시 네이버 맵 또는 카카오맵 활용
//        val uri = Uri.parse(locLatLng)
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        startActivity(intent)
//    }
    private fun handleLocationDataReady() {
        if (!isLocationDataReady) {
            return
        }

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.nearbyToiletsmap) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)
    }


    //map 정보 가져 오기 완료 확인 Callback
    val mapReadyCallback = object : OnMapReadyCallback {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onMapReady(map: GoogleMap) {
            //Log.d(TAG, "위도: ${latitude}, 경도: ${longitude}")
            googleMap = map //map 정보 가져오기 완료 시 멤버변수에 저장
            var dlat = latitude
            var dlogi = longitude

            if (dlat != null && dlogi != null) {
                //위도 경도 위치 설정
                val targetLocation = LatLng(dlat, dlogi)

                //지오코딩
                geocoder.getFromLocation(dlat, dlogi, 5) { addresses ->
                    CoroutineScope(Dispatchers.Main).launch {
                        exact_location = addresses.get(0).getAddressLine(0).toString()
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
            showData("위도: ${latitude}, 경도: ${longitude}")
        }
    }

    fun addMarker(targetLoc: LatLng, exactLocation: String?) {
        Log.d(TAG, "exact_location : ${exactLocation}")
        val markerOptions: MarkerOptions = MarkerOptions() // 마커를 표현하는 Option 생성
        markerOptions.position(targetLoc) // 필수
            .title("현재 위치")
            .snippet(exactLocation ?: "Location information not available")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        centerMarker = googleMap.addMarker(markerOptions) // 지도에 마커 추가, 추가마커 반환
        centerMarker?.showInfoWindow() // 마커 터치 시 InfoWindow 표시
//        centerMarker?.tag = "database_id"
        // 마커에 관련 정보(Object) 저장


        // 마커 클릭 이벤트 처리
        googleMap.setOnMarkerClickListener { marker ->
            Toast.makeText(requireContext(), marker.tag.toString(), Toast.LENGTH_SHORT).show()
            false // true일 경우 이벤트처리 종료이므로 info window 미출력
        }

        // 마커 InfoWindow 클릭 이벤트 처리
        googleMap.setOnInfoWindowClickListener { marker ->
            Toast.makeText(requireContext(), marker.title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showData(data: String) {
        mBinding?.tvData?.let {
            it.setText(it.text.toString() + "\n$data")
        }
    }

}