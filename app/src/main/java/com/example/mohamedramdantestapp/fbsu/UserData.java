package com.example.mohamedramdantestapp.fbsu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;

import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohamed Ramdan on 10/18/2016.
 */

public class UserData extends AppCompatActivity {



    private ProfilePictureView profilePictureView;
    private LinearLayout infoLayout;
    private TextView profileEmail;
    private TextView profileGender;
    private TextView profileName;
    //private TextView profileBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.user_data);

        profileEmail = (TextView)findViewById(R.id.email);
        profileName = (TextView)findViewById(R.id.name);
        profileGender = (TextView)findViewById(R.id.gender);
        //profileBirth = (TextView)findViewById(R.id.birthDay);
        infoLayout = (LinearLayout)findViewById(R.id.layout_info);
        profilePictureView = (ProfilePictureView)findViewById(R.id.image);

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        } else {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("Home", response.toString());
                            setProfileToView(object);
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();

        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void setProfileToView(JSONObject jsonObject) {
        try {
            profileEmail.setText(jsonObject.getString("email"));
            profileGender.setText(jsonObject.getString("gender"));
            profileName.setText(jsonObject.getString("name"));
            //profileBirth.setText(jsonObject.getString("birthday"));
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));
            infoLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

