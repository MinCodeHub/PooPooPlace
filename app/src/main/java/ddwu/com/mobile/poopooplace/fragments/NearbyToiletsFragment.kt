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
//    private lateinit var currentLoc: Location

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
//            showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    currentLoc.latitude,
                    currentLoc.longitude,
                    1
                ) { addresses ->
                    CoroutineScope(Dispatchers.Main).launch {
                        showData(addresses.get(0).toString())
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


    private fun showData(data: String) {
        mBinding?.tvData?.let {
            it.setText(it.text.toString() + "\n$data")
        }
    }

}