package com.flutter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreateAccount : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity__createaccount)

        //set app to use phone full screen width
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initialize()
    }
    private fun initialize (){
        val buttonCreate:Button = findViewById<Button>(R.id.btnCreateAccount)
        buttonCreate.setOnClickListener { userNewAccount()}
    }
    //Function for account creation using Firebase
    //Make sure to import all gradle dependencies before using Firebase
    private fun userNewAccount(){
        val mAuth = FirebaseAuth.getInstance()

        //initialize user inputs
        val etFirstname = findViewById<EditText>(R.id.userFirstName) as EditText
        val etSecondName = findViewById<EditText>(R.id.userSecondName) as EditText
        val etEmailAddress = findViewById<EditText>(R.id.emailaddress) as EditText
        val etPassword = findViewById<EditText>(R.id.newPassword) as EditText
        val mProgressBar = ProgressBar(this)
        val mDatabase = FirebaseDatabase.getInstance()
        val referenceUser = mDatabase.reference!!.child("Users")!!

        //set input types

        val firstname = etFirstname.text.toString()
        val secondname = etSecondName.text.toString()
        val email = etEmailAddress.text.toString()
        val password = etPassword.text.toString()

        //check if they are empty

        if (!firstname.isEmpty() && !secondname.isEmpty() && !email.isEmpty() && !password.isEmpty()){
        mAuth!!
            .createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                Log.d("Auth","Creation Successfully " + task.result!!.user.uid)
                Toast.makeText(this@CreateAccount,"Your Account has been created successfully.",Toast.LENGTH_LONG).show()
                //verify email address
                verifyEmail()

                //update users info

                val userId = mAuth!!.currentUser!!.uid

                val currentUserDb = mDatabase.reference!!.child(userId)!!
                currentUserDb.child("First_name").setValue(firstname)
                currentUserDb.child("Second_Name").setValue(secondname)

                updateUserInfoUI()
            }
        } else {
            Log.d("Error","Error authenticating" )
            Toast.makeText(this@CreateAccount,"Please enter all details",Toast.LENGTH_LONG).show()
            return
        }
    }
    private fun verifyEmail() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@CreateAccount,
                    "Verification email sent to " + mUser.getEmail(),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e("TAG", "sendEmailVerification", task.exception)
                Toast.makeText(
                    this@CreateAccount,
                    "Failed to send verification email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun updateUserInfoUI(){
        val intent = Intent(this@CreateAccount,WelcomeUser::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}