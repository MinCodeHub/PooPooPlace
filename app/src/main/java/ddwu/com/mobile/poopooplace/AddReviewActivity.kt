package ddwu.com.mobile.poopooplace

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
import ddwu.com.mobile.poopooplace.data.MemoDao
import ddwu.com.mobile.poopooplace.data.MemoDatabase
import ddwu.com.mobile.poopooplace.data.MemoDto
import ddwu.com.mobile.poopooplace.databinding.ActivityMapBinding
import ddwu.com.mobile.poopooplace.databinding.ActivityReviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddReviewActivity : AppCompatActivity() {
    var TAG = "g"
    val addMemoBinding by lazy {
        ActivityReviewBinding.inflate(layoutInflater)
    }
    val REQUEST_IMAGE_CAPTURE = 1

    val memoDB: MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao: MemoDao by lazy {
        memoDB.memoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(addMemoBinding.root)

        addMemoBinding.btnAdd.setOnClickListener {
            if (currentPhotoFileName != null) {
                val memo = addMemoBinding.tvAddMemo.text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    memoDao.insertMemo(MemoDto(0, currentPhotoFileName!!, memo))
                }

                Toast.makeText(this@AddReviewActivity, "New memo is added!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        addMemoBinding.ivAddPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        addMemoBinding.btnCancel.setOnClickListener {
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    setPic()
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {   // 원본 사진 요청
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) { // 카메라 앱 확인
            val photoFile: File? = try {    // 고화질 사진을 저장할 파일 생성
                createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "ddwu.com.mobile.poopooplace.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    lateinit var currentPhotoPath: String   // 현재 이미지 파일의 경로 저장
    var currentPhotoFileName: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File("${storageDir?.path}/${timeStamp}.jpg")

        currentPhotoFileName = file.name
        currentPhotoPath = file.absolutePath
        Log.d("경로", currentPhotoPath )
        return file
    }


    private fun setPic() {
        Glide.with(this)
            .load(File(currentPhotoPath))
            .into(addMemoBinding.ivAddPhoto)
    }
}