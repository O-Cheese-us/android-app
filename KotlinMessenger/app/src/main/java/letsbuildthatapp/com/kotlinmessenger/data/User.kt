package letsbuildthatapp.com.kotlinmessenger.data

const val COLLECTION_USERS = "users"

const val USER_UID = "uid"
const val USER_USERNAME = "username"
const val USER_CHEESE_COUNTER = "cheeseCounter"

data class User(
    val uid: String = "",
    var username: String = "",
    var cheeseCounter: Int = 0
){

}