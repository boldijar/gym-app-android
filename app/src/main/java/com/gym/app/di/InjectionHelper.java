package com.gym.app.di;

import com.gym.app.Shaorma;

public final class InjectionHelper {

    private static ApplicationComponent sApplicationComponent;

    public static void init(Shaorma shaorma) {
        sApplicationComponent = shaorma.getApplicationComponent();
    }

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }
}