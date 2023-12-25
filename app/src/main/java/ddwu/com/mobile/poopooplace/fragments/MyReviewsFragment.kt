package ddwu.com.mobile.poopooplace.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.poopooplace.AddReviewActivity
import ddwu.com.mobile.poopooplace.R
import ddwu.com.mobile.poopooplace.ShowReviewActivity
import ddwu.com.mobile.poopooplace.data.MemoDao
import ddwu.com.mobile.poopooplace.data.MemoDatabase
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding
import ddwu.com.mobile.poopooplace.data.RestroomRoot
import ddwu.com.mobile.poopooplace.network.PublicToiletPOIServiceeAPIService
import ddwu.com.mobile.poopooplace.ui.RestroomAdapter
import ddwu.com.mobile.poopooplace.ui.ui.MemoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyReviewsFragment : Fragment() {
    private var mBinding: FragmentMyReviewsBinding? = null
    private val TAG = "MyReviewsFragment"

    val memoDB: MemoDatabase by lazy {
        MemoDatabase.getDatabase(requireContext())
    }

    val memoDao: MemoDao by lazy {
        memoDB.memoDao()
    }

    val adapter: MemoAdapter by lazy {
        MemoAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyReviewsBinding.inflate(inflater, container, false)
        mBinding = binding

        mBinding!!.rvMemo.adapter = adapter
        mBinding!!.rvMemo.layoutManager = LinearLayoutManager(requireContext())

        mBinding!!.btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddReviewActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickListener(object : MemoAdapter.OnMemoItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), ShowReviewActivity::class.java)
                intent.putExtra("memoDto", adapter.memoList?.get(position))
                startActivity(intent)
            }
        })

        //롱클릭 시 삭제
        adapter.setOnItemLongClickListener(object : MemoAdapter.OnMemoItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                val selectedMemo = adapter.memoList?.get(position)
                selectedMemo?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            memoDao.deleteMemo(it)
                        }
                    }
                }
            }
        })
        showAllMemo()

        return mBinding?.root
    }

    fun showAllMemo() {
        CoroutineScope(Dispatchers.Main).launch {
            memoDao.getAllMemos().collect { memos ->
                adapter.memoList = memos
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }


}
