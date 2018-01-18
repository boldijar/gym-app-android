package com.gym.app.parts.gallery;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 2018.01.l8
 */

interface GalleryView {

    void setPhotos(List<String> photos);
    void displayError();
}
