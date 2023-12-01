package ddwu.com.mobile.poopooplace.data
annotation class JsonProperty(val value: String)
annotation class SerializedName(val value: String)

//    최상위 시작 부분을 나타내는 클래스
data class Root(
        @JsonProperty("SearchPublicToiletPOIService")
        val searchPublicToiletPoiservice: SearchPublicToiletPoiservice,
    )


data class SearchPublicToiletPoiservice(
        @JsonProperty("list_total_count")
        val listTotalCount: ListTotalCount,
        @JsonProperty("RESULT")
        val result: Result,
        @SerializedName("row")
        val restrooms : List<Restroom>,
    )


data class ListTotalCount(
        @JsonProperty("_text")
        val text: String,
    )

    data class Result(
        @JsonProperty("CODE")
        val code: Code,
        @JsonProperty("MESSAGE")
        val message: Message,
    )

    data class Code(
        @JsonProperty("_text")
        val text: String,
    )

    data class Message(
        @JsonProperty("_text")
        val text: String,
    )

    data class Restroom(
        @JsonProperty("POI_ID")
        val poiId: String,
        @JsonProperty("FNAME")
        val fname: String,
        @JsonProperty("ANAME")
        val aname: String,
        @JsonProperty("CNAME")
        val cname: Map<String, Any>,
        @JsonProperty("CENTER_X1")
        val centerX1: String,
        @JsonProperty("CENTER_Y1")
        val centerY1: String,
        @JsonProperty("X_WGS84")
        val xWgs84: String,
        @JsonProperty("Y_WGS84")
        val yWgs84: String,
        @JsonProperty("INSERTDATE")
        val insertdate: String,
        @JsonProperty("UPDATEDATE")
        val updatedate: String,
    )

