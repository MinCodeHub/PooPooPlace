package ddwu.com.mobile.poopooplace.network

import ddwu.com.mobile.poopooplace.data.RestroomRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 요청정보
interface PublicToiletPOIServiceeAPIService {
    @GET("SearchPublicToiletPOIService/1/5/")
    fun getToiletData(
        @Query("key") key: String
    ): Call<RestroomRoot> // 결과 반환 타입
}
