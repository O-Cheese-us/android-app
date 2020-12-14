package us.oh.ocheeseus

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_username.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
                    var randomKasimir = generateAvailableRandomCheeseTag()
                    edittext_userName_cheeseTag.setText(randomKasimir)


                    button_userName_generateRandom.setOnClickListener {
                        randomKasimir = generateAvailableRandomCheeseTag()
                        edittext_userName_cheeseTag.setText(randomKasimir)
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

    interface CheeseTagAvailableCallback {
        fun onCallback(isAvailable: Boolean)
    }

    private fun isCheeseTagAvailable(cheeseTag: String): Boolean {
        val docRef = db.collection("users").whereEqualTo("cheeseTag", cheeseTag)
        docRef.get().addOnSuccessListener { documents ->
                if (documents.isEmpty){
                    Log.d(TAG, "true")
                } else {
                    Log.d(TAG, "false")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Unable to check whether cheese tag $cheeseTag is available: ", e)
            }
        return true
    }

    private fun retrieveCheeseTagCollection(collectionName: String, list: ArrayList<String>, callback: CheeseTagInitCallback){
        val docRef = db.collection("cheese_tag_generator").document("cheese_tags")
        docRef.collection(collectionName).get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "Found $collectionName entry: ${document.id}")
                list.add(document.id)
            }
            callback.onCallback()
        }.addOnFailureListener { e ->
            Log.w(TAG, "Unable to retrieve cheese tag generator $collectionName from Firestore: ", e)
        }
    }

    private fun initCheeseTagCollections(callback: CheeseTagInitCallback){
        retrieveCheeseTagCollection("adjectives", adjectiveList, callback)
        retrieveCheeseTagCollection("cheeses", cheeseList, callback)
        retrieveCheeseTagCollection("nouns", nounList, callback)
    }

    private suspend fun isCheesyCheeseReallyCheesy(cheeseTag: String): Boolean {

        if (cheeseTag.isEmpty()){
            return false
        }


        var isNameAvailable = true
        val docRef = db.collection("users").whereEqualTo("cheeseTag", cheeseTag)

        runBlocking {
            var result = docRef.get().await().documents
            if (result.isNotEmpty()){
                isNameAvailable = false
                Log.d(TAG, "Ja leckst mi am Orsch, es gibt scho an User der $cheeseTag hoasst!")
            }
        }

        return isNameAvailable
    }

    private fun generateAvailableRandomCheeseTag() : String {
        var currentCheeseTag = ""
        GlobalScope.launch {
            while (!isCheesyCheeseReallyCheesy(currentCheeseTag)) {
                currentCheeseTag = generateRandomCheeseTag()
            }
        }

        while (currentCheeseTag.isEmpty()){
            Log.d(TAG ,"OMG I am stuck in a loop, how annoying is that!")
        }

        return currentCheeseTag;
    }

    private fun generateRandomCheeseTag(): String {
        return adjectiveList.random() + cheeseList.random() + nounList.random()
    }

}
