package us.oh.ocheeseus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class UsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        Toast.makeText(this, "Griasdi Username!", Toast.LENGTH_SHORT).show()
    }
}
