package us.oh.ocheeseus.data

import android.net.Uri

const val COLLECTION_USERS = "users"

const val USER_UID = "uid"
const val USER_USERNAME = "username"
const val USER_CHEESE_COUNTER = "cheeseCounter"
const val USER_PROFILE_PICTURE_URL = "profilePictureUrl"

data class User(
    val uid: String = "",
    var username: String = "",
    var cheeseCounter: Int = 0,
    var profilePictureUrl: Uri? = null
){

}