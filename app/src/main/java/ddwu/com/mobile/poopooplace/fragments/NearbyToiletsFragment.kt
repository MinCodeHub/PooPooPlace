package ddwu.com.mobile.poopooplace.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.data.RestroomRoot
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding
import ddwu.com.mobile.poopooplace.databinding.FragmentNearbyToiletsBinding
import ddwu.com.mobile.poopooplace.network.PublicToiletPOIServiceeAPIService
import ddwu.com.mobile.poopooplace.ui.RestroomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NearbyToiletsFragment : Fragment() {

    private var mBinding: FragmentNearbyToiletsBinding? = null
    private val TAG ="NEARBYTOLIETS"
    lateinit var adapter : RestroomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binging = FragmentNearbyToiletsBinding.inflate(inflater,container,false)
        mBinding = binging
        return mBinding?.root
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RestroomAdapter() // Initialize your adapter

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.restroom_url)) // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PublicToiletPOIServiceeAPIService::class.java)

        // Change the method name to match your actual API service interface
        mBinding?.btnSearch?.setOnClickListener {
            val apiCallback = object : Callback<RestroomRoot> {
                override fun onResponse(
                    call: Call<RestroomRoot>,
                    response: Response<RestroomRoot>
                ) {
                    if (response.isSuccessful) {
                        val root: RestroomRoot? = response.body()
                        adapter.restrooms = root?.searchPublicToiletPoiservice?.restrooms
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d(TAG, "Unsuccessful Response")
                    }
                }

                override fun onFailure(call: Call<RestroomRoot>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }
            }

            // Change the method call to match your actual API service interface
            val apiCall: Call<RestroomRoot> =
                service.getToiletData(resources.getString(R.string.restroom_key))
            apiCall.enqueue(apiCallback)
        }
    }
}