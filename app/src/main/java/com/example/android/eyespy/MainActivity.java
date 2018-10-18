package com.example.android.eyespy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference("users");



private Button buttonscan,uploadbutton;
private TextView textView;
private EditText editText;
private String s ="null";
//Firebase object

String email = "";



private IntentIntegrator qrscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up the local cache
        SharedPreferences sharedPreferences = getSharedPreferences("savefile",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("state","").isEmpty()){
            saveinfo();
        }
        //objects for views
        buttonscan = (Button) findViewById(R.id.scan_button);
        uploadbutton =(Button) findViewById(R.id.uploadbutton);
        editText = (EditText) findViewById(R.id.email_id);
        textView =(TextView) findViewById(R.id.counter);


        //scan object initialization
        qrscan = new IntentIntegrator(this);
        buttonscan.setOnClickListener(this);

    }



    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textview
                    //textView.setText(obj.getString("code"));
                    s = obj.getString("code");
                    check();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrscan.initiateScan();
    }


    public void check() {
        SharedPreferences sharedPreferences = getSharedPreferences("savefile", Context.MODE_PRIVATE);
        SharedPreferences rewrite = getSharedPreferences("savefile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = rewrite.edit();
        String a = sharedPreferences.getString("a", "");
        String b = sharedPreferences.getString("b", "");
        String c = sharedPreferences.getString("c", "");
        String d = sharedPreferences.getString("d", "");
        String e = sharedPreferences.getString("e", "");

        String scores  = sharedPreferences.getString("score","");
        int score = Integer.parseInt(scores); //string to int

        if (a.compareTo(s) == 0) {
            score = score + 1;      //update score by 1
            s = Integer.toString(score);         //saving score as integer in s
            textView.setText(s);       //displaying total score ..hmm needs wrk here
            editor.putString("a", "NULL");  //delete the scanned qr from list
            editor.putString("score",s);    //writing score to sharedpreferences
            editor.apply();
        } else if (b.compareTo(s) == 0) {
            score = score + 1;
            s = "" + score;
            textView.setText(s);
            editor.putString("b", "NULL");
            editor.putString("score",s);
            editor.apply();
        } else if (c.compareTo(s) == 0) {
            score = score + 1;
            s = "" + score;
            textView.setText(s);
            editor.putString("c", "NULL");
            editor.putString("score",s);
            editor.apply();
        } else if (d.compareTo(s) == 0) {
            score = score + 1;
            s = "" + score;
            textView.setText(s);
            editor.putString("d", "NULL");
            editor.putString("score",s);
            editor.apply();
        } else if (e.compareTo(s) == 0) {
            score = score + 1;
            s = "" + score;
            textView.setText(s);
            editor.putString("e", "NULL");
            editor.putString("score",s);
            editor.apply();
        } else {
            textView.setText(scores);
            //Toast.makeText(this, "You scanned this once! :( " + "\n"
               //     + "Find another one. Go shoosh!", Toast.LENGTH_LONG).show();
            //textView.setText("You scanned this once! :( " + "\n" + "Find another one. Go shoosh!");
            editor.apply();
        }
    }

    public void saveinfo(){             //this fn gets called only once
        SharedPreferences sharedPreferences = getSharedPreferences("savefile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state","NOT_EMPTY");
        editor.putString("score","0");
        editor.putString("a","ABCDE");
        editor.putString("b","FGHIJ");
        editor.putString("c","KLMNO");
        editor.putString("d","PQRST");
        editor.putString("e","UVWX");
        editor.apply();

    }

    public void upload(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("savefile", Context.MODE_PRIVATE);
        String upscore = sharedPreferences.getString("score", "");
        SharedPreferences rewrite = getSharedPreferences("savefile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = rewrite.edit();
        email=editText.getText().toString();
        String sme = email;
        //Map<String,String> map = new HashMap<String, String>();
        //map.put(email,s);
        String db = "Email ID = "+ email + " : Count = " + upscore ;
        myRef.child(sme).setValue(db);
        editor.putString("score","0");  //RESETS COUNTER AND update score in sharedpref to 0

    }

}


