package us.oh.ocheeseus

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_username.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import us.oh.ocheeseus.data.COLLECTION_USERS
import us.oh.ocheeseus.data.CheeseTagGenerator

private const val TAG = "UsernameActivity"

class UsernameActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val cheeseTagGenerator = CheeseTagGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        Toast.makeText(this, "Griazzdi Username!", Toast.LENGTH_SHORT).show()

        loadCurrentCheeseTagFromFireStore()
        GlobalScope.launch {
            cheeseTagGenerator.initLists()
        }

        button_userName_generateRandom.setOnClickListener {
            GlobalScope.launch {
                var randomCheeseTag = cheeseTagGenerator.generateAvailableCheeseTag()
                edittext_userName_cheeseTag.setText(randomCheeseTag)
            }
        }

    }

    private fun loadCurrentCheeseTagFromFireStore(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val docRef = db.collection(COLLECTION_USERS).document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null)
                edittext_userName_cheeseTag.setText(document.data?.get("cheeseTag").toString())
            else {
                Log.e(TAG, "No username found for user with UID $uid")
            }
        }
            .addOnFailureListener {
                Log.w(TAG, "Error while trying to load cheese tag for user with UID $uid", it)
            }
    }

}
