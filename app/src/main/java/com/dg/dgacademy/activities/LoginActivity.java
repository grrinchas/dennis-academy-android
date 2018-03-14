package com.dg.dgacademy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.api.Response;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.result.Credentials;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import api.AuthenticateMutation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity implements Validator.ValidationListener{

    @NotEmpty
    @Email
    @BindView(R.id.login_email)
    EditText email;

    @NotEmpty
    @BindView(R.id.login_password)
    EditText password;

    private Validator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.login_to_sign_up)
    public void onClickLoginToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.login)
    public void onClickLogin() {
        validator.validate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(Credentials credentials) {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailure(AuthenticationException e) {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValidationSucceeded() {
        DgApplication.login(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
