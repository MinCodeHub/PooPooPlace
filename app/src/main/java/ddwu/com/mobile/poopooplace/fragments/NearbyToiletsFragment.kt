package ddwu.com.mobile.poopooplace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding
import ddwu.com.mobile.poopooplace.databinding.FragmentNearbyToiletsBinding


class NearbyToiletsFragment : Fragment() {

    private var mBinding: FragmentNearbyToiletsBinding? = null
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


}