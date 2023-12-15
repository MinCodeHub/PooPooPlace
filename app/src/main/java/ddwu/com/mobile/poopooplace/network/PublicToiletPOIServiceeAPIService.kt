package ddwu.com.mobile.poopooplace.network

import ddwu.com.mobile.poopooplace.data.RestroomRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 요청정보
interface PublicToiletPOIServiceeAPIService {
    @GET("{key}/json/SearchPublicToiletPOIService/1/20")
    fun getToiletData(
        @Path("key") key: String,
        @Query("ANAME") ANAME :String,
    ): Call<RestroomRoot> // 결과 반환 타입
}
