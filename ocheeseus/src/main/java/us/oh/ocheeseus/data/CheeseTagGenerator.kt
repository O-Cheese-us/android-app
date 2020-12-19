package us.oh.ocheeseus.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception

private const val TAG = "CheeseTagGenerator"

class CheeseTagGenerator() {

    private val db = FirebaseFirestore.getInstance()

    private var adjectiveList = ArrayList<String>()
    private var cheeseList = ArrayList<String>()
    private var nounList = ArrayList<String>()

    fun initLists(){
        runBlocking {
            adjectiveList = retrieveCheeseTagCollection("adjectives")
            cheeseList = retrieveCheeseTagCollection("cheeses")
            nounList = retrieveCheeseTagCollection("nouns")
        }
    }

    private suspend fun retrieveCheeseTagCollection(collectionName: String): ArrayList<String> {
        val docRef = db.collection("cheese_tag_generator").document("cheese_tags")
        var documents = docRef.collection(collectionName).get().await()
        var list = ArrayList<String>()
        for (document in documents) {
            Log.d(TAG, "Found $collectionName entry: ${document.id}")
            list.add(document.id)
        }
        return list
    }

    private fun generateRandomCheeseTag(): String? {
        if (adjectiveList.isEmpty() || cheeseList.isEmpty() || nounList.isEmpty()){
            Log.e(TAG, "At least one list for random cheese tag generation has not been initialized yet!")
            return null
        }
        return adjectiveList.random() + cheeseList.random() + nounList.random()
    }

    private suspend fun isCheeseTagAvailable(cheeseTag: String?): Boolean {
        if (cheeseTag == null || cheeseTag.isEmpty()){
            return false;
        }
        val docRef = db.collection("users").whereEqualTo("cheeseTag", cheeseTag)
        var result = docRef.get().await()
        return result.isEmpty
    }

    fun generateAvailableCheeseTag(): String? {
        var currentCheeseTag = generateRandomCheeseTag()
        runBlocking {
            while (!isCheeseTagAvailable(currentCheeseTag))
            currentCheeseTag = generateRandomCheeseTag()
        }
        return currentCheeseTag;
    }

}