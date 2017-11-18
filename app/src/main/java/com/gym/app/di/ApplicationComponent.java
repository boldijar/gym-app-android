package com.gym.app.di;

import com.gym.app.Shaorma;
import com.gym.app.data.observables.SaveCoursesObservable;
import com.gym.app.data.observables.SaveProductsObservable;
import com.gym.app.data.observables.UpdateCourseObservable;
import com.gym.app.parts.authentication.AuthenticationPresenter;
import com.gym.app.parts.findcourses.FindCoursesPresenter;
import com.gym.app.parts.findcourses.day_courses.DayCoursesPresenter;
import com.gym.app.parts.authentication.register.RegisterPresenter;
import com.gym.app.parts.mycourses.MyCoursesPresenter;
import com.gym.app.parts.shop.ShopPresenter;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(Shaorma shaorma);

    void inject(AuthenticationPresenter authenticationPresenter);

    void inject(ShopPresenter shopPresenter);

    void inject(SaveProductsObservable saveProductsObservable);

    void inject(RegisterPresenter registerPresenter);

    void inject(FindCoursesPresenter findCoursesPresenter);

    void inject(DayCoursesPresenter dayCoursesPresenter);

    void inject(SaveCoursesObservable saveCoursesObservable);

    void inject(UpdateCourseObservable updateCourseObservable);

    void inject(MyCoursesPresenter myCoursesPresenter);
}