package com.gym.app.parts.profile;

import com.gym.app.data.model.User;

/**
 * @author flaviuoprea
 * @since 2017.11.12
 */

public interface ProfileView {

    void showError();

    void showUser(User value);

    void updateMessage();
}
