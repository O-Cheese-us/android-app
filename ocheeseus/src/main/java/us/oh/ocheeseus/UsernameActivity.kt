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

private const val TAG = "CounterActivity"

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
            override fun onCallback(
                adjectivesLoaded: Boolean,
                cheesesLoaded: Boolean,
                nounsLoaded: Boolean
            ) {
                if (adjectivesLoaded && cheesesLoaded && nounsLoaded){
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
        fun onCallback(adjectivesLoaded: Boolean, cheesesLoaded: Boolean, nounsLoaded: Boolean)
    }

    private fun initCheeseTagCollections(callback: CheeseTagInitCallback){

        var adjectivesLoaded = false
        var cheesesLoaded = false
        var nounsLoaded = false

        val docRef = db.collection("cheese_tag_generator").document("cheese_tags")

        // Retrieve Adjectives
        docRef.collection("adjectives").get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "Found adjective ${document.id}")
                adjectiveList.add(document.id)
            }
            adjectivesLoaded = true
            callback.onCallback(adjectivesLoaded, cheesesLoaded, nounsLoaded)
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Unable to retrieve cheese tag generator adjectives from Firestore: ", exception)
        }

        // Retrieve Cheeses
        docRef.collection("cheeses").get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "Found cheese ${document.id}")
                cheeseList.add(document.id)
            }
            cheesesLoaded = true
            callback.onCallback(adjectivesLoaded, cheesesLoaded, nounsLoaded)
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Unable to retrieve cheese tag generator cheeses from Firestore: ", exception)
        }

        // Retrieve Nouns
        docRef.collection("nouns").get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "Found noun ${document.id}")
                nounList.add(document.id)
            }
            nounsLoaded = true
            callback.onCallback(adjectivesLoaded, cheesesLoaded, nounsLoaded)
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Unable to retrieve cheese tag generator nouns from Firestore: ", exception)
        }
    }

    private fun generateRandomCheeseTag(): String {
        if (adjectiveList.isEmpty() || cheeseList.isEmpty() || nounList.isEmpty()){
            return "SomethingWentTerriblyWrong"
        }
        var adjective = adjectiveList.random()
        var cheese = cheeseList.random()
        var noun = nounList.random()

        return adjective + cheese + noun
    }

}
