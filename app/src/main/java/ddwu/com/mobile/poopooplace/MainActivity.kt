package ddwu.com.mobile.poopooplace
import ddwu.com.mobile.poopooplace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ddwu.com.mobile.poopooplace.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var mBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //네비게이션들을 답는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        //네비게이션 컨트롤러
        val navController = navHostFragment.navController

        //바텀 네비게인션 뷰와 네비게이션을 묶어준다.
        NavigationUI.setupWithNavController(mBinding.bottomNavi,navController)
    }
}