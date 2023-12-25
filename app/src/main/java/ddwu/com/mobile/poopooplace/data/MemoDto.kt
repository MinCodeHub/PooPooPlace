package ddwu.com.mobile.poopooplace.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo_table")
data class MemoDto(
    @PrimaryKey (autoGenerate = true)
    val id: Long,
    var photoName: String,
    var memo: String) : Serializable {
    override fun toString(): String {
        return "${id} (${photoName}) : $memo"
    }
}
