package com.frankdevacap.frogchat


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity(){

    private val GOOGLE_SIGN_IN = 100

    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de firebase Completa")
        analytics.logEvent("InitScreen", bundle)


        setup()
        session()

        authLayout.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()

        btnEnviarCodigo.setOnClickListener {

            if (editTextPhone.text.isNotEmpty()){
                authLayout.visibility = View.VISIBLE
                verify ()
            }

        }

        btnCodex.setOnClickListener {

            authenticate()
        }



    }
    private fun setup(){

        btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

                            finish()

                        }else {
                            Toast.makeText(this, "LA AUNTETICACION CON GOOGLE NO SE REALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }catch (e: ApiException){
                Toast.makeText(this, "LA AUNTETICACION CON GOOGLE NO SE REALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show()


            }

        }
    }

    private fun verificationCallbacks(){

        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signIn(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verification, p1)

                verificationId = verification


            }


        }

    }

    private fun verify (){

        verificationCallbacks()

        val phone = "+52" + editTextPhone.text.toString()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, mCallbacks )


    }

    private fun signIn(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                }
            }


    }

    private fun authenticate(){
        val codex = editTextNumberVerification.text.toString()

        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, codex)
        signIn(credential)

    }

    private fun session() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

    }


}