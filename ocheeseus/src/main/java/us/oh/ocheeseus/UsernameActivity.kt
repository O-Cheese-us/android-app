package us.oh.ocheeseus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "CounterActivity"

class UsernameActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        Toast.makeText(this, "Griazzdi Username!", Toast.LENGTH_SHORT).show()
    }
}
