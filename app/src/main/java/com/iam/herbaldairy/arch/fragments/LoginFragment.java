package com.iam.herbaldairy.arch.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.widget.Decorator;
import com.iam.herbaldairy.widget.text.Text;

public class LoginFragment extends Fragment{

    private Type type = Type.Login;

    private View view;
    private EditText loginET;
    private EditText passwordET;
    private EditText emailET;
    private FrameLayout saveButton;
    private Text saveText;
    private Text signUp;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);

        loginET = (EditText) view.findViewById(R.id.login);
        passwordET = (EditText) view.findViewById(R.id.password);
        emailET = (EditText) view.findViewById(R.id.email);
        saveButton = (FrameLayout) view.findViewById(R.id.login_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case Login:

                        break;
                    case Registration:

                        break;
                }
            }
        });
        saveText = (Text) view.findViewById(R.id.save_text);
        signUp = (Text) view.findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case Login:
                        type = Type.Registration;
                        emailET.setHeight((int) Decorator.getpixels(54));
                        signUp.setText("LOG IN");
                        saveText.setText("SIGN UP");
                        break;
                    case Registration:
                        type = Type.Login;
                        signUp.setText("SIGN UP");
                        emailET.setHeight(0);
                        saveText.setText("LOG IN");
                        break;
                }
            }
        });


        return view;
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            String login = strings[0];
            String password = strings[1];

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private enum Type {
        Registration, Login
    }
}
