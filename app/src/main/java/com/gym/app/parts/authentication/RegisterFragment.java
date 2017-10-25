package com.gym.app.parts.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.gym.app.R;
import com.gym.app.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author catalinradoiu
 * @since 2017.10.25
 */

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.register_name_input)
    AppCompatEditText mNameInput;

    @BindView(R.id.register_email_input)
    AppCompatEditText mEmailInput;

    @BindView(R.id.register_password_input)
    AppCompatEditText mPasswordInput;

    @BindView(R.id.register_button)
    Button mRegisterButton;

    @BindView(R.id.register_back_button)
    Button mBackButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationNavigation) getActivity()).goBack();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }
}
