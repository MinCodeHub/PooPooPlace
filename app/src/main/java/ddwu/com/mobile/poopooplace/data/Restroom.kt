package ddwu.com.mobile.poopooplace.data
import com.google.gson.annotations.SerializedName

data class RestroomRoot(
    @SerializedName("searchPublicToiletPoiservice")
    val searchPublicToiletPoiservice: SearchPublicToiletPoiservice
)


data class SearchPublicToiletPoiservice(
    @SerializedName("listTotalCount")
    val listTotalCount: ListTotalCount,
    @SerializedName("result")
    val result: Result,
    @SerializedName("restrooms")
    val restrooms: List<Restroom>
)

data class ListTotalCount(
    @SerializedName("_text")
    val text: String
)

data class Result(
    @SerializedName("CODE")
    val code: Code,
    @SerializedName("MESSAGE")
    val message: Message
)

data class Code(
    @SerializedName("_text")
    val text: String
)

data class Message(
    @SerializedName("_text")
    val text: String
)

data class Restroom(
    @SerializedName("POI_ID")
    val poiId: String,
    @SerializedName("FNAME")
    val fname: String,
    @SerializedName("ANAME")
    val aname: String,
    @SerializedName("CNAME")
    val cname: Map<String, Any>,
    @SerializedName("CENTER_X1")
    val centerX1: String,
    @SerializedName("CENTER_Y1")
    val centerY1: String,
    @SerializedName("X_WGS84")
    val xWgs84: String,
    @SerializedName("Y_WGS84")
    val yWgs84: String,
    @SerializedName("INSERTDATE")
    val insertdate: String,
    @SerializedName("UPDATEDATE")
    val updatedate: String
)
