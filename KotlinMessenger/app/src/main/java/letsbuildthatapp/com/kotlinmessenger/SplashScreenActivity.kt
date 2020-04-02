package letsbuildthatapp.com.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash_screen.*
import letsbuildthatapp.com.kotlinmessenger.data.USER_CHEESE_COUNTER
import letsbuildthatapp.com.kotlinmessenger.data.USER_UID
import java.lang.Exception

private const val TAG = "SplashScreenActivity"

class SplashScreenActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (auth.currentUser == null){
            signUpAnonymously()
        } else {
            Log.d(TAG,"Already logged in with user UID = ${auth.currentUser!!.uid}")
        }

        startSplashScreenAnimation()
    }

    private fun startSplashScreenAnimation(){
        val animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        splashScreen_imageView_cheeseEmoji.startAnimation(animSlideDown)

        val animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        splashScreen_imageView_logo.startAnimation(animSlideUp)

        val background = object : Thread() {
            override fun run () {
                try {
                    sleep(2000)
                    val intent = Intent(baseContext, MenuActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.d(TAG, "Got exception during splash screen animations:  $e")
                }
            }
        }
        background.start()
    }


    private fun signUpAnonymously(){

        Log.d(TAG, "Trying to sign in anonymously...")
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener{

            if (it.isSuccessful){
                Log.d(TAG, "Anonymous sign in successful!")
                Log.d(TAG, "UID after (not to confuse with anus) login: ${auth.uid}")
                storeUserInFireStore()
            } else {
                Log.d(TAG, "Exception while trying to sign in anonymously: ${it.exception}")
            }
        }

        FirebaseAuth.getInstance().addAuthStateListener {
            Log.d(TAG, "Got user: $it with uid ${it.currentUser?.uid}")
        }
    }

    private fun storeUserInFireStore() {

        Log.d(TAG, "Storing user ${auth.currentUser?.uid} in Firestore.")

        val user = hashMapOf(
            USER_UID to auth.uid,
            USER_CHEESE_COUNTER to 0
        )

        db.collection("users").document("${auth.currentUser?.uid}").set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written, yo cheese man!")}
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
                return@addOnFailureListener
            }
    }

}
