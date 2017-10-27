
# Coding guidelines
* Use camel case.
* Member of classes must have m Prefix, why? because is the AOSP convention, and it's good to be used. https://stackoverflow.com/questions/2092098/why-do-most-fields-class-members-in-android-tutorial-start-with-m
ex: mNameTextView instead of nameTextView. You can set in Android Studio that you want to have m prefix from the settings, and it will help you creating the getters and setters.
* No extra lines. Don't add empty lines, only if really needed. (To separate some coding logic maybe is ok, but if you accidentaly add new empty lines, please remove them before creating merge request)
* Activities can only be started using createIntent static method. ex: startActivity(HomeActivity.createIntent(this).
* Always extend BaseActivity & BaseFragment
* When adding strings into strings.xml, if you add them for a new screen, please add some commend before them, to have them sorted (same for colors.xml, pls set them ordered)
* If you see a style of a view that should be in 2 places or more, please create custom style in style.xml, to avoid redundancy.
* Do not use hardcoded dimens, string, colors etc. Declare resources for that and use them for easier maintainability.

# Contributing
Please choose some intuitive commit names, and try to only add your own changes to the project. If you accidentally add new lines or change something that is not related to the ticket, don't commit that code.
