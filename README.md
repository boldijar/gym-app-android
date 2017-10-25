
# Coding guidelines
* Use camel case.
* Member of classes must have m Prefix, why? because is the AOSP convention, and it's good to be used. https://stackoverflow.com/questions/2092098/why-do-most-fields-class-members-in-android-tutorial-start-with-m
ex: mNameTextView instead of nameTextView. You can set in Android Studio that you want to have m prefix from the settings, and it will help you creating the getters and setters.
* No extra lines. Don't add empty lines, only if really needed. (To separate some coding logic maybe is ok, but if you accidentaly add new empty lines, please remove them before creating merge request)
