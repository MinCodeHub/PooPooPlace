package ddwu.com.mobile.poopooplace.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding
import ddwu.com.mobile.poopooplace.data.RestroomRoot
import ddwu.com.mobile.poopooplace.network.PublicToiletPOIServiceeAPIService
import ddwu.com.mobile.poopooplace.ui.RestroomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyReviewsFragment : Fragment() {
    private var mBinding: FragmentMyReviewsBinding? = null
    private val TAG = "MyReviewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyReviewsBinding.inflate(inflater, container, false)
        mBinding = binding
        return mBinding?.root
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }


}
