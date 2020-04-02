package us.oh.ocheeseus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login", "Attempt login with email $email and password ***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        return@addOnCompleteListener
                    }

                    // else if successful
                    Log.d("Login", "Successfully logged in user with UID: ${it.result?.user?.uid}")

                }
                .addOnFailureListener {
                    Log.d("Login", "Failed to login user: ${it.message}")
                    Toast.makeText(this, "Failed to login user: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

}