package com.gym.app.parts.settings;

import com.gym.app.data.model.AtTheGym;

/**
 * @author Paul
 * @since 2018.01.17
 */

public interface SettingsView {

    void checkInSuccess(boolean checkedIn);

    void numberOfUsers(AtTheGym noUsers);
}
