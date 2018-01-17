package com.gym.app.parts.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.gym.app.R;
import com.gym.app.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author catalinradoiu
 * @since 2017.10.25
 */

public class LoginFragment extends BaseFragment {

    //A valid password must contain at least 8 characters, one uppercase, one lowercase and one letter
    private static final String VALID_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

    @BindView(R.id.login_email_layout)
    TextInputLayout mEmailLayout;

    @BindView(R.id.login_email_input)
    TextInputEditText mEmailInput;

    @BindView(R.id.login_password_layout)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.login_password_input)
    TextInputEditText mPasswordInput;

    @BindView(R.id.register_new_button)
    Button mRegisterButton;

    @BindView(R.id.login_button)
    Button mLoginButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.register_new_button)
    void onRegisterNewButtonClick() {
        ((AuthenticationNavigation) getActivity()).goToRegister();
    }

    @OnClick(R.id.login_button)
    void onLoginButtonClick() {
        boolean mValidEmail = !mEmailInput.getText().toString().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(mEmailInput.getText().toString()).matches();
        boolean mValidPassword =!mPasswordInput.getText().toString().isEmpty() &&
                mPasswordInput.getText().toString().matches(VALID_PASSWORD_REGEX);
        if (!mValidEmail) {
            mEmailLayout.setError(getString(R.string.invalid_email_address));
        } else {
            mEmailLayout.setError("");
        }
        if (!mValidPassword) {
            mPasswordLayout.setError(getString(R.string.invalid_password));
        } else {
            mPasswordLayout.setError("");
        }
        if (mValidEmail && mValidPassword) {
            ((AuthenticationActivity) getActivity()).doLogin(mEmailInput.getText().toString(),
                    mPasswordInput.getText().toString());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }
}
