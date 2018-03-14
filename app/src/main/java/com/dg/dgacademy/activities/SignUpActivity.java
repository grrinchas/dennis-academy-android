package com.dg.dgacademy.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.result.Credentials;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.draft.AllPrivateDraftsActivity;
import com.dg.dgacademy.model.DraftsEvent;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends Activity implements Validator.ValidationListener {

    @NotEmpty
    @Email
    @BindView(R.id.sign_up_email)
    EditText email;

    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    @BindView(R.id.sign_up_password)
    EditText password;

    @NotEmpty
    @Pattern(regex = "^[a-zA-Z0-9_\\-]*$")
    @BindView(R.id.sign_up_username)
    EditText username;

    @ConfirmPassword
    @BindView(R.id.sign_up_password_confirmation)
    EditText confirmPassword;

    private Validator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.sign_up_to_login)
    public void onClickSignUpToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.sign_up)
    public void onClickSignUp(){
        this.validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        DgApplication.signUp(username.getText().toString(), email.getText().toString(), password.getText().toString());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpSuccess(Credentials credentials) {
        Toast.makeText(this, "Account has been created", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpFailure(AuthenticationException e) {
        Toast.makeText(this, e.getDescription(), Toast.LENGTH_LONG).show();
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
