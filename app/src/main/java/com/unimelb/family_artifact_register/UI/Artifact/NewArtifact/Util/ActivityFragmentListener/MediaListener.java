package com.unimelb.family_artifact_register.UI.Artifact.NewArtifact.Util.ActivityFragmentListener;

import android.net.Uri;

import java.util.List;

/**
 * activity with media source and needed to be interacted with the fragment
 */
public interface MediaListener {
    /**
     * clear all media data
     */
    void clearData();

    /**
     * @return media data list
     */
    List<Uri> getData();

    /**
     * take in data and compress it and record it in the activity
     *
     * @param data media data, can be image or video
     * @param type MediaProcessHelper's processing data Type
     */
    void addData(Uri data, int type);

    /**
     * 1: image 2: video
     *
     * @return the type of the media data stored
     */
    int getMediaType();

    /**
     * 1: image 2: video
     *
     * @param type the type of the media data stored
     */
    void setMediaType(int type);
}
