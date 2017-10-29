package com.gym.app.parts.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @BindView(R.id.login_email_input)
    EditText mEmailInput;

    @BindView(R.id.login_password_input)
    EditText mPasswordInput;

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
        ((AuthenticationActivity) getActivity()).doLogin(mEmailInput.getText().toString(),
                mPasswordInput.getText().toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }
}
