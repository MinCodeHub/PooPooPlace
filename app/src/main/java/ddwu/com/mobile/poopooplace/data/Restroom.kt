package ddwu.com.mobile.poopooplace.data
import com.google.gson.annotations.SerializedName

data class RestroomRoot(
    @SerializedName("SearchPublicToiletPOIService")
    val searchPublicToiletPoiservice: SearchPublicToiletPoiservice
)


data class SearchPublicToiletPoiservice(
    @SerializedName("row")
    val restrooms: List<Restroom>
)

data class Restroom(
    @SerializedName("POI_ID")
    val poiId: String,
    @SerializedName("FNAME")
    val fname: String,
    @SerializedName("ANAME")
    val aname: String,
    @SerializedName("CNAME")
    val cname: String,
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
