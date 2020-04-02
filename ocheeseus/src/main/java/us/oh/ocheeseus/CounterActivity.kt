package us.oh.ocheeseus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_counter.*
import us.oh.ocheeseus.data.COLLECTION_USERS
import us.oh.ocheeseus.data.USER_CHEESE_COUNTER
import us.oh.ocheeseus.data.User

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
        val uid = FirebaseAuth.getInstance().uid ?: ""

        Log.d(TAG, "Incrementing cheese counter for user with UID $uid")

        db.collection(COLLECTION_USERS).document("$uid").update(
            USER_CHEESE_COUNTER, FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "Increment successful: $it")}
            .addOnFailureListener { e -> Log.w(TAG, "Error while incrementing cheese counter for user $uid: ", e)
                return@addOnFailureListener
            }
    }

    private fun initCheeseCounter() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        Log.d(TAG, "Initializing cheese counter for user $uid...")
        val docRef = db.collection(COLLECTION_USERS).document(uid)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed: ", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
                Log.d(TAG, "Current data: ${snapshot.data}")
                var user = snapshot.toObject(User::class.java)
                if (user?.cheeseCounter != null){
                    counter_edittext_cheesecounter.setText(user?.cheeseCounter?.toString())
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

}
