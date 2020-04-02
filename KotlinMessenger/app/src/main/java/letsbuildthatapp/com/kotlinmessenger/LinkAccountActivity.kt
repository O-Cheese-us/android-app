package letsbuildthatapp.com.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class LinkAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_account)
        Toast.makeText(this, "Griasdi Link Account!", Toast.LENGTH_SHORT).show()
    }
}
