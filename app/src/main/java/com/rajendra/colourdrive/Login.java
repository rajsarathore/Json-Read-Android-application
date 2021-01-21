package com.rajendra.colourdrive;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText username,password;
    private CheckBox showpassword;
    public static UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.Edit_Username);
        password=(EditText)findViewById(R.id.Edit_Pass);
        showpassword=(CheckBox)findViewById(R.id.checkbox_ShowPassword);

        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        ((Button)findViewById(R.id.Button_Login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AttemptLogin login = new AttemptLogin();
                    login.execute(new String[]{username.getText().toString(), password.getText().toString()});
                }catch (Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });
    }



    private class AttemptLogin extends AsyncTask<String,String,JSONObject>{
        String user, pass;
        @Override
        protected JSONObject doInBackground(String[] objects) {
            user=objects[0];
            pass=objects[1];
            JSONObject jsonObject=null;
            String url="http://www.beta.colourdrive.in/apk/user_detail.php";
            //?username=$username&password=$password";
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("username", objects[0]));
            urlParameters.add(new BasicNameValuePair("password",objects[1]));
            String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
            url += "?" + paramString;

            HttpClient clients= new DefaultHttpClient();
            HttpGet httpGet=new HttpGet(url);
            Log.e("url",url);
            try {

            HttpResponse httpResponse=clients.execute(httpGet);
            InputStream inputStream=httpResponse.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder=new StringBuilder();
            String line=null;
            while ((line=bufferedReader.readLine())!= null)
                builder.append(line);

            inputStream.close();

                Log.e("json data",builder.toString());

                jsonObject=new JSONObject(builder.toString());

            }catch (Exception e){
                Log.e("http Error",e.getMessage());
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject!=null) {
                try {
                    if (jsonObject.getString("username").equals(user) && jsonObject.getString("password").equals(pass)) {

                        userData = new UserData();
                        userData.setUsername(jsonObject.getString("username"));
                        userData.setPassword(jsonObject.getString("password"));
                        userData.setId(jsonObject.getString("id"));
                        userData.setName(jsonObject.getString("name"));
                        userData.setCity(jsonObject.getString("city"));
                        userData.setCompany(jsonObject.getString("company"));
                        userData.setAge(jsonObject.getInt("age"));

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.e("Json Error", e.getMessage());
                }
            }else {
                Log.e("null jsonObject", "Please check the network connetion");
                Toast.makeText(getApplicationContext(),"Please check the Network Connection",Toast.LENGTH_LONG).show();
            }

        }
    }
}
