package my.significs.gep.faceid.model



import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * [Affirmation] is the data class to represent the Affirmation text and imageResourceId
 */
data class UserModel(
    val name : String,
    val email : String,
    val employeeID : String,
    val cardID: String = "",
)