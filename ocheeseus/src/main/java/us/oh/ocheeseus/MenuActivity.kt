package us.oh.ocheeseus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button_menu_add_username.setOnClickListener {
            val intent = Intent(baseContext, UsernameActivity::class.java)
            startActivity(intent)
        }

        button_menu_link_account.setOnClickListener {
            val intent = Intent(baseContext, LinkAccountActivity::class.java)
            startActivity(intent)
        }

        button_menu_upload_profile_picture.setOnClickListener {
            val intent = Intent(baseContext, ProfilePictureActivity::class.java)
            startActivity(intent)
        }

    }
}
