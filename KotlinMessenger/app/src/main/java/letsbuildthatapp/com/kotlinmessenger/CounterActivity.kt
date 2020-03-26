package letsbuildthatapp.com.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_counter.*
import kotlinx.android.synthetic.main.content_home.*

private const val TAG = "CounterActivity"

class CounterActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Opened CounterActivity")
        setContentView(R.layout.activity_counter)

        initCheeseCounter()

        counter_button_kasgfressn.setOnClickListener{
            incrementCheeseCounter()
        }

    }

    private fun incrementCheeseCounter() {

        val incrementedValue = counter_edittext_cheesecounter.text.toString().toInt() + 1
        counter_edittext_cheesecounter.setText(incrementedValue.toString())

        val uid = FirebaseAuth.getInstance().uid ?: ""

        val user = hashMapOf(
            "uid" to uid,
            "cheesecounter" to incrementedValue
        )

        db.collection("users").document("$uid").set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written, yo cheese man!")}
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
                return@addOnFailureListener
            }

    }

    private fun initCheeseCounter() {
        Log.d(TAG, "Initializing cheese counter...")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    Log.d(TAG, "Found data : ${doc.data}")
                    var user = doc.toObject(User::class.java)
                    if (user?.cheesecounter != null){
                        val currentCheeseCount = user?.cheesecounter
                        counter_edittext_cheesecounter.setText(user?.cheesecounter?.toString())
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener {
                exception -> Log.d(TAG, "Got exception ", exception)
            }

    }

    data class User(
        var uid: String = "",
        var username: String = "",
        var cheesecounter: Int = 0
    )

}
