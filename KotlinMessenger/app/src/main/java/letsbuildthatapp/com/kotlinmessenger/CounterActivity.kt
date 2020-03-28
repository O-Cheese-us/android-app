package letsbuildthatapp.com.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_counter.*
import kotlinx.android.synthetic.main.content_home.*
import letsbuildthatapp.com.kotlinmessenger.data.*

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

        //val incrementedValue = counter_edittext_cheesecounter.text.toString().toInt() + 1
        //counter_edittext_cheesecounter.setText(incrementedValue.toString())

        val uid = FirebaseAuth.getInstance().uid ?: ""

        db.collection(COLLECTION_USERS).document("$uid").update(USER_CHEESE_COUNTER, FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written, yo cheese man! $it")}
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)
                return@addOnFailureListener
            }

    }

    private fun initCheeseCounter() {

        Log.d(TAG, "Initializing cheese counter...")
        val uid = FirebaseAuth.getInstance().uid ?: ""
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
