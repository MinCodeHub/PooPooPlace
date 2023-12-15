package ddwu.com.mobile.poopooplace.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.poopooplace.MapActivity
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.data.Restroom
import ddwu.com.mobile.poopooplace.data.RestroomRoot
import ddwu.com.mobile.poopooplace.databinding.FragmentNearbyToiletsBinding
import ddwu.com.mobile.poopooplace.network.PublicToiletPOIServiceeAPIService
import ddwu.com.mobile.poopooplace.ui.OnItemClickListener
import ddwu.com.mobile.poopooplace.ui.RestroomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NearbyToiletsFragment : Fragment() {

    private lateinit var mBinding: FragmentNearbyToiletsBinding
    private val TAG = "NEARBYTOLIETS"
    private lateinit var adapter: RestroomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = RestroomAdapter()
        val binding = FragmentNearbyToiletsBinding.inflate(inflater, container, false)
        mBinding = binding
        mBinding.rvRestRoom.adapter = adapter
        mBinding.rvRestRoom.layoutManager = LinearLayoutManager(requireContext())

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.restroom_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PublicToiletPOIServiceeAPIService::class.java)

        mBinding.btnSearch.setOnClickListener {
            val targetKeyword = mBinding.tvItem.text.toString()

            val apiCallback = object : Callback<RestroomRoot> {
                override fun onResponse(
                    call: Call<RestroomRoot>,
                    response: Response<RestroomRoot>
                ) {
                    if (response.isSuccessful) {
                        val root: RestroomRoot? = response.body()
                        Log.d(TAG, "Response: $root")
                        adapter.restrooms = root?.searchPublicToiletPoiservice?.restrooms
                        adapter.notifyDataSetChanged()
                        Log.d(TAG, "Successful Response")
                        Log.d(TAG, "Response: ${adapter.restrooms}")
                        Log.d(TAG, "Raw Response: ${response.raw().toString()}")

                    } else {
                        Log.d(TAG, "Unsuccessful Response: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<RestroomRoot>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }
            }


            val apiCall: Call<RestroomRoot> =
                service.getToiletData(resources.getString(R.string.restroom_key), targetKeyword)
            Log.d(TAG, "API URL: ${apiCall.request().url()}")

            apiCall.enqueue(apiCallback)
        }
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(restroom: Restroom) {
                // 아이템 클릭 시 처리할 내용을 여기에 작성
                // 예: Intent를 사용한 화면 전환
                val intent = Intent(requireContext(), MapActivity::class.java)
                //intent.putExtra("key", value) // 필요한 경우 데이터 전달
                startActivity(intent)
            }
        }
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
