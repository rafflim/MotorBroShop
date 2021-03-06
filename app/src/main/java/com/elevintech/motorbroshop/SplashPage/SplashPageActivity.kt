package com.elevintech.motorbroshop.SplashPage

import androidx.appcompat.app.AppCompatActivity
import com.elevintech.motorbroshop.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.elevintech.motorbroshop.Dashboard.DashboardActivity
import com.elevintech.motorbroshop.Database.MotorBroDatabase
import com.elevintech.motorbroshop.Login.LoginActivity
import com.elevintech.motorbroshop.Model.UserType
import com.elevintech.motorbroshop.Register.RegisterShop
import com.elevintech.motorbroshop.Register.SelectLocation
import com.google.firebase.auth.FirebaseAuth

class SplashPageActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_page)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {


            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()

            val currentUser = auth.currentUser
            if (currentUser != null) {

                val db = MotorBroDatabase()
                db.getUserType { userType ->

                    if (userType == UserType.Type.OWNER) {

                        db.getOwner {
                            val user = it

                            if (user.shopId.isEmpty()) {
                                val intent = Intent(applicationContext, RegisterShop::class.java)
                                startActivity(intent)
                            } else {
                                val intent =
                                    Intent(applicationContext, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else if (userType == UserType.Type.EMPLOYEE) {

                        db.getEmployee {
                            val user = it

                            if (user.shopId.isEmpty()) {
                                val intent = Intent(applicationContext, RegisterShop::class.java)
                                startActivity(intent)
                            } else {
                                val intent =
                                    Intent(applicationContext, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }

            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

//            val sharedPref = SharedPreferences(applicationContext)
//            //val isNotFirstTimeOpening = sharedPref.getValueBool("IS_NOT_FIRST_TIME_OPENING_APP"
//            val isNotFirstTimeOpening = false




        }
    }
}
