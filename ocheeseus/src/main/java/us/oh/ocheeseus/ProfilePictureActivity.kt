package us.oh.ocheeseus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile_picture.*
import us.oh.ocheeseus.data.*
import java.util.*

private const val TAG = "ProfilePictureActivity"

class ProfilePictureActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var selectedPhotoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)
        Toast.makeText(this, "Griasdi Profile Picture!", Toast.LENGTH_SHORT).show()

        button_profilePicture_select.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        button_profilePicture_upload.setOnClickListener {
            if (selectedPhotoUri == null) {
                Toast.makeText(this, "In order to upload a picture, you have to select one first!", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageToFirebaseStorage(selectedPhotoUri!!)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what selected image was...
            Log.d(TAG, "Photo was selected.")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            imageview_profilePicture_profilePicture.setImageBitmap(bitmap)
            button_profilePicture_select.alpha = 0f

        }
    }

    private fun uploadImageToFirebaseStorage(selectedPhotoUri : Uri) {

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
                    updateUserProfilePictureInForeStore(it.toString())
                }

            }.addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }

    }

    private fun updateUserProfilePictureInForeStore(profilePictureUrl: String) {

        db.collection(COLLECTION_USERS).document(auth.uid.toString()).update(
            USER_PROFILE_PICTURE_URL, profilePictureUrl
        )
            .addOnSuccessListener {
                Log.d(TAG, "Profile Picture Update successful: $it")
                Toast.makeText(this, "Picture successfully updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error while changing profile picture for user ${auth.uid}: ", e)
                return@addOnFailureListener
            }
    }

}
