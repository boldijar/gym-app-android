package com.gym.app.parts.profile;

import com.gym.app.data.model.GymUser;

/**
 * @author flaviuoprea
 * @since 2017.11.12
 */

public interface ProfileView {

    void showError();

    void showUser(GymUser value);

    void updateMessage();
}
