package com.komal.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username,password;
    Button signup,login;
    Boolean signUpModeActive;
    ConstraintLayout layout;
    ImageView imageView;
     SharedPreferences sp;


    public void logIn(View view){

        if(username.getText().toString().matches("") || password.getText().toString().matches("")){
            Toast.makeText(this,"Please enter username and password",Toast.LENGTH_SHORT).show();
        }
        else{

                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                                 if(user!=null){
                                     Toast.makeText(MainActivity.this,"Log In successfully",Toast.LENGTH_SHORT).show();
                                     Intent i=new Intent(MainActivity.this,Home.class);
                                     startActivity(i);

                                 }
                                 else{
                                     Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                 }
                        }
                    });

            }



        }




    public void signUp(View view){

        if(username.getText().toString().matches("") || password.getText().toString().matches("")){
            Toast.makeText(this,"Please enter username and password",Toast.LENGTH_SHORT).show();
        }
        else{

                ParseUser user=new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this,"Sign Up successfully",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(MainActivity.this,Home.class);
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       username=(EditText)findViewById(R.id.username);
       password=(EditText)findViewById(R.id.password);
       signup=(Button)findViewById(R.id.signup);
        login=(Button)findViewById(R.id.login);
       imageView=(ImageView)findViewById(R.id.imageView);

       /*
         logging user permanently

        sp= getSharedPreferences("logged",MODE_PRIVATE);
       if(sp.getBoolean("logged",false)){
           Intent i=new Intent(MainActivity.this,Home.class);
           startActivity(i);
       }
       else{
           Intent i=new Intent(MainActivity.this,Home.class);
           startActivity(i);
           sp.edit().putBoolean("logged",true).apply();
       }
*/

       layout=(ConstraintLayout)findViewById(R.id.layout);
       layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(v.getId()==R.id.layout || v.getId()==R.id.imageView){
                   InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                   inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
               }

           }
       });


    }
    @Override
    public void onClick(View v) {


    }
}