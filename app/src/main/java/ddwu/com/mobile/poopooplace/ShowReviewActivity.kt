package ddwu.com.mobile.poopooplace

import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobile.poopooplace.data.MemoDto
import ddwu.com.mobile.poopooplace.databinding.ActivityMapBinding
import ddwu.com.mobile.poopooplace.databinding.ActivityShowReviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class ShowReviewActivity : AppCompatActivity() {
    val TAG = "ShowMemoActivityTag"

    val showMemoBinding by lazy {
        ActivityShowReviewBinding.inflate(layoutInflater)
    }

    lateinit var memoDto : MemoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showMemoBinding.root)

        showMemoBinding.btnModify.setOnClickListener {
            Toast.makeText(this, "Implement modifying data", Toast.LENGTH_SHORT).show()
        }

        showMemoBinding.btnClose.setOnClickListener {
            finish()
        }

        memoDto = intent.getSerializableExtra("memoDto") as MemoDto

        showMemoBinding.tvMemo.setText(memoDto.memo)


        val photo = File (getExternalFilesDir(Environment.DIRECTORY_PICTURES), memoDto.photoName)

        Glide.with(this)
            .load(photo)
            .into(showMemoBinding.ivPhoto)
    }
}