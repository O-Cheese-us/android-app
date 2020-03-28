package letsbuildthatapp.com.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import io.opencensus.stats.Aggregation
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.Exception

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        splashScreen_imageView_cheeseEmoji.startAnimation(animSlideDown)

        val animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        splashScreen_imageView_logo.startAnimation(animSlideUp)

        val background = object : Thread() {
            override fun run () {
                try {
                    Thread.sleep(2000)

                    val intent = Intent(baseContext, CounterActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
