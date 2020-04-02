package letsbuildthatapp.com.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class ProfilePictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)
        Toast.makeText(this, "Griasdi Profile Picture!", Toast.LENGTH_SHORT).show()
    }
}
