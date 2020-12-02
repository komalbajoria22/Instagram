package com.komal.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements View.OnClickListener{


    ListView listView;
Button button;



    public void getPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //adding menu item in

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);
        menuInflater.inflate(R.menu.upload_image,menu);
        menuInflater.inflate(R.menu.logout, menu);


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //performing actions on menu item click

        if (item.getItemId() == R.id.profile) {

            //passing value from this activity to profile activity

            Intent intent = new Intent(Home.this, Profile.class);
            intent.putExtra("usernam",ParseUser.getCurrentUser().getUsername());
            startActivity(intent);
        }

        if(item.getItemId()==R.id.upload_image){

            //asking for permission to access gallery

            if (Build.VERSION.SDK_INT >23) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }

                else{
                    getPhoto();
                }
            }

        }

        if (item.getItemId() == R.id.logout) {


            Intent i = new Intent(Home.this, MainActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        listView=(ListView)findViewById(R.id.listView);

        //adding existing usernames in the database in the listview

        final ArrayList<String> usernames= new ArrayList<String>();
        final ArrayAdapter arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1,usernames);
        ParseQuery<ParseUser> query= ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseUser user:objects){

                            usernames.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                    listView.setAdapter(arrayAdapter);
                }
                else{
                    e.printStackTrace();
                }

                }

            });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //passing item value that we clicked from this activity to profile activity

                Intent i=new Intent(Home.this,Profile.class);
                i.putExtra("usernam",usernames.get(position));
                startActivity(i);



            }
        });


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //uploading images on parse server

        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            Uri selectImage=data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectImage);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray=stream.toByteArray();  //uploading in byte array type
                ParseFile file=new ParseFile("image.png",byteArray); //name of file(image name)
                ParseObject object=new ParseObject("Image");  //class
                object.put("image",file);//column
                object.put("username", ParseUser.getCurrentUser().getUsername());  //column
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(Home.this,"Uploaded successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Home.this,"Error occured! Try agin later.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {

    }
}