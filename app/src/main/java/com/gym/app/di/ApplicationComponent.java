package com.gym.app.di;

import com.gym.app.Shaorma;
import com.gym.app.data.observables.SaveCoursesObservable;
import com.gym.app.data.observables.SaveProductsObservable;
import com.gym.app.data.observables.UpdateCourseObservable;
import com.gym.app.fragments.DrawerFragment;
import com.gym.app.parts.authentication.AuthenticationPresenter;
import com.gym.app.parts.authentication.register.RegisterPresenter;
import com.gym.app.parts.create_course.CreateCoursePresenter;
import com.gym.app.parts.findcourses.FindCoursesPresenter;
import com.gym.app.parts.findcourses.day_courses.DayCoursesPresenter;
import com.gym.app.parts.gallery.GalleryPresenter;
import com.gym.app.parts.mycourses.MyCoursesPresenter;
import com.gym.app.parts.profile.ProfilePresenter;
import com.gym.app.parts.settings.SettingsPresenter;
import com.gym.app.parts.notes.NotesPresenter;
import com.gym.app.parts.profile.ProfilePresenter;
import com.gym.app.parts.shop.ShopPresenter;
import com.gym.app.parts.trainedcourses.TrainedCoursesPresenter;
import com.gym.app.parts.updatecourse.UpdateCoursePresenter;

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

    void inject(CreateCoursePresenter createCoursePresenter);

    void inject(ProfilePresenter profilePresenter);

    void inject(NotesPresenter notesPresenter);

    void inject(SettingsPresenter settingsPresenter);

    void inject(GalleryPresenter galleryPresenter);

    void inject(DrawerFragment drawerFragment);

    void inject(TrainedCoursesPresenter trainedCoursesPresenter);

    void inject(UpdateCoursePresenter updateCoursePresenter);
}