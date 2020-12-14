package us.oh.ocheeseus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_username.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

private const val TAG = "UsernameActivity"

class UsernameActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var adjectiveList = ArrayList<String>()
    private var cheeseList = ArrayList<String>()
    private var nounList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        button_userName_generateRandom.isClickable = false
        button_userName_generateRandom.isEnabled = false
        Toast.makeText(this, "Griazzdi Username!", Toast.LENGTH_SHORT).show()

        initCheeseTagCollections(object: CheeseTagInitCallback {
            override fun onCallback() {
                if (adjectiveList.isNotEmpty() && cheeseList.isNotEmpty() && nounList.isNotEmpty()){
                    Log.d(TAG, "All lists initialized!")
                    button_userName_generateRandom.isClickable = true
                    button_userName_generateRandom.isEnabled = true
                    var cheeseTag = generateRandomCheeseTag()
                    edittext_userName_cheeseTag.setText(cheeseTag)
                    button_userName_generateRandom.setOnClickListener {
                        cheeseTag = generateRandomCheeseTag()
                        edittext_userName_cheeseTag.setText(cheeseTag)
                    }
                } else {
                    Log.d(TAG, "Not all lists initialized yet.")
                }
            }
        })

    }

    interface CheeseTagInitCallback {
        fun onCallback()
    }

    private fun retrieveCheeseTagCollection(collectionName: String, list: ArrayList<String>, callback: CheeseTagInitCallback){
        val docRef = db.collection("cheese_tag_generator").document("cheese_tags")
        docRef.collection(collectionName).get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "Found $collectionName entry: ${document.id}")
                list.add(document.id)
            }
            callback.onCallback()
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Unable to retrieve cheese tag generator $collectionName from Firestore: ", exception)
        }
    }

    private fun initCheeseTagCollections(callback: CheeseTagInitCallback){
        retrieveCheeseTagCollection("adjectives", adjectiveList, callback)
        retrieveCheeseTagCollection("cheeses", cheeseList, callback)
        retrieveCheeseTagCollection("nouns", nounList, callback)
    }

    private fun generateRandomCheeseTag(): String {
        return adjectiveList.random() + cheeseList.random() + nounList.random()
    }

}
