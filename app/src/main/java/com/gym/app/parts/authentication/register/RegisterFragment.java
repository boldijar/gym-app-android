package com.gym.app.parts.authentication.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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

    //A valid password must contain at least 8 characters, one uppercase, one lowercase and one letter
    private static final String VALID_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private static final String VALID_NAME_REGEX = "[A-Z][a-z]+[ ][A-Z][a-z]+";

    @BindView(R.id.register_name_layout)
    TextInputLayout mNameLayout;

    @BindView(R.id.register_name_input)
    TextInputEditText mNameInput;

    @BindView(R.id.register_email_layout)
    TextInputLayout mEmailLayout;

    @BindView(R.id.register_email_input)
    TextInputEditText mEmailInput;

    @BindView(R.id.register_password_layout)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.register_password_input)
    TextInputEditText mPasswordInput;

    @BindView(R.id.register_password_confirmation_layout)
    TextInputLayout mPasswordConfirmationLayout;

    @BindView(R.id.register_password_confirmation_input)
    TextInputEditText mPasswordConfirmationInput;

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
        boolean mValidPassword = !mPasswordInput.getText().toString().isEmpty() &&
                mPasswordInput.getText().toString().matches(VALID_PASSWORD_REGEX);
        boolean mValidPasswordMatching = mPasswordInput.getText().toString()
                .equals(mPasswordConfirmationInput.getText().toString());
        boolean mValidEmail = !mEmailInput.getText().toString().isEmpty() &&
                mPasswordInput.getText().toString().matches(VALID_PASSWORD_REGEX);
        boolean mValidFullName = !mNameInput.getText().toString().isEmpty() &&
                mNameInput.getText().toString().matches(VALID_NAME_REGEX);

        if (!mValidPassword) {
            mPasswordLayout.setError(getString(R.string.invalid_password));
        } else {
            mPasswordLayout.setError("");
        }

        if (!mValidEmail) {
            mEmailLayout.setError(getString(R.string.invalid_email_address));
        } else {
            mEmailLayout.setError("");
        }

        if (mValidPassword) {
            if (!mValidPasswordMatching) {
                mPasswordConfirmationLayout.setError(getString(R.string.password_do_not_match));
            } else {
                mPasswordConfirmationLayout.setError("");
            }
        }

        if (!mValidFullName) {
            mNameLayout.setError(getString(R.string.invalid_name));
        } else {
            mNameLayout.setError("");
        }

        if (mValidFullName && mValidEmail && mValidPassword && mValidPasswordMatching) {
            mRegisterPresenter.register(mNameInput.getText().toString(), mEmailInput.getText().toString(),
                    mPasswordInput.getText().toString());
        }
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
    public void displayRegistrationError(RegisterErrorType errorType) {
        if (mRetrySnackbar != null) {
            mRetrySnackbar.dismiss();
        }
        if (getView() != null) {
            String errorMessage = "";
            if (errorType == RegisterErrorType.CONNECTION_ERROR) {
                errorMessage = getString(R.string.no_internet_connection);
            } else if (errorType == RegisterErrorType.EMAIL_IN_USE_ERROR) {
                errorMessage = getString(R.string.email_address_in_use);
            }
            mRetrySnackbar = Snackbar.make(getView(), errorMessage,
                    BaseTransientBottomBar.LENGTH_LONG);
            if (errorType == RegisterErrorType.CONNECTION_ERROR) {
                mRetrySnackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRegisterPresenter.register(mNameInput.getText().toString(),
                                mEmailInput.getText().toString(),
                                mPasswordInput.getText().toString());
                    }
                });
            }
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
        mPasswordConfirmationInput.setText("");
    }
}
