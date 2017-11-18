package com.gym.app.parts.authentication.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.fragments.BaseFragment;
import com.gym.app.parts.authentication.AuthenticationNavigation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author catalinradoiu
 * @since 2017.10.25
 */

public class RegisterFragment extends BaseFragment implements RegisterView {

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

    private RegisterPresenter mRegisterPresenter;
    private Toast mOperationStatus;
    private Snackbar mRetrySnackbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
    }

    @OnClick(R.id.register_back_button)
    void onBackButtonClick() {
        ((AuthenticationNavigation) getActivity()).goBack();
    }

    @OnClick(R.id.register_button)
    void onRegisterClick() {
        mRegisterPresenter.register(mNameInput.getText().toString(), mEmailInput.getText().toString(),
                mPasswordInput.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRegisterPresenter.destroySubscriptions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void displayRegistrationSuccess() {
        if (mOperationStatus != null) {
            mOperationStatus.cancel();
        }
        clearFields();
        mOperationStatus = Toast.makeText(getContext(), getString(R.string.register_successful),
                Toast.LENGTH_SHORT);
        mOperationStatus.show();
    }

    @Override
    public void displayRegistrationError() {
        if (mRetrySnackbar != null) {
            mRetrySnackbar.dismiss();
        }
        if (getView() != null) {
            mRetrySnackbar = Snackbar.make(getView(), getString(R.string.no_internet_connection),
                    BaseTransientBottomBar.LENGTH_LONG);
            mRetrySnackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRegisterPresenter.register(mNameInput.getText().toString(),
                            mEmailInput.getText().toString(),
                            mPasswordInput.getText().toString());
                }
            });
            mRetrySnackbar.show();
        }
    }

    private void initPresenter() {
        mRegisterPresenter = new RegisterPresenter(this);
    }

    private void clearFields() {
        mEmailInput.setText("");
        mNameInput.setText("");
        mPasswordInput.setText("");
    }
}
