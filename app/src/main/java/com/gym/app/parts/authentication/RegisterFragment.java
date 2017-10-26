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

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.register_name_input)
    EditText mNameInput;

    @BindView(R.id.register_email_input)
    EditText mEmailInput;

    @BindView(R.id.register_password_input)
    EditText mPasswordInput;

    @BindView(R.id.register_button)
    Button mRegisterButton;

    @BindView(R.id.register_back_button)
    Button mBackButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.register_back_button)
    void onBackButtonClick(){
        ((AuthenticationNavigation) getActivity()).goBack();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }
}
