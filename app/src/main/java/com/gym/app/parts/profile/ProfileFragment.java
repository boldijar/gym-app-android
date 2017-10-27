package com.gym.app.parts.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ProfileFragment extends BaseHomeFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @OnClick(R.id.profile_image)
    void clickedImage(){
        Toast.makeText(getContext(), "to be edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected int getTitle() {
        return R.string.profile;
    }
}
