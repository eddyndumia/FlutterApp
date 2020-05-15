package com.flutter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import kotlinx.android.synthetic.main.activity__createaccount.*
import kotlinx.android.synthetic.main.activity__welcomeuser.*

class WelcomeUser: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity__welcomeuser)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN )

        //display user name on welcome text

        welcomeBanner()
    }
private fun welcomeBanner(){
    //this function grabs user data from database
    //initialize Firebase globals
    val welcomeTxt = findViewById<TextView>(R.id.welcomeText) as TextView
    val mAuth = FirebaseAuth.getInstance()
    val mDatabase = FirebaseDatabase.getInstance().reference
    fun currentUserInfo() :DatabaseReference = mDatabase.child("Users").child(mAuth.currentUser!!.uid)

    currentUserInfo().addValueEventListener(object : ValueEventListener{
        @SuppressLint("SetTextI18n")
        override fun onDataChange(snapshot: DataSnapshot) {
            val username = snapshot.child("First_name")
            val names:String? = username.value as String
            welcomeTxt.text = "Welcome $names"
            Log.d("Auth","Grabbed user $username")
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("Error", "Error grabbing data")
        }
    })

    //display the welcome text

}
}


