package letsbuildthatapp.com.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener{
            performRegister()
        }

        already_have_account_textView_register.setOnClickListener{
            Log.d(TAG, "Try to show login activity")
            // launch the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
    }

    var selectedPhotoUri: Uri? = null;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what selected image was...
            Log.d(TAG, "Photo was selected.")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            photo_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f

        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are mandatory!", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Email is $email")
        Log.d(TAG, "Password is $password")


        // Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }

                // else if successful
                Log.d(TAG, "Successfully created user with UID: ${it.result?.user?.uid}")
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {

        Log.d(TAG, "Got selected photo uri: $selectedPhotoUri")

        if (selectedPhotoUri == null) {
            return
        }

        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirestore(it.toString())
                }

            }.addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }

    }

    private fun saveUserToFirestore(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""

        val user2 = User(uid, username_edittext_register.text.toString(), profileImageUrl, 0)

        val userMap = hashMapOf(
            "uid" to uid,
            "username" to username_edittext_register.text.toString(),
            "cheeseCounter" to 0,
            "profileImageUrl" to profileImageUrl

            )


        db.collection("users").document("$uid").set(userMap)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written, yo cheese man!")}
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String, val cheeseCounter: Int)