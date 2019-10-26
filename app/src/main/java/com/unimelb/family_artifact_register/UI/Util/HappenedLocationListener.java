package com.unimelb.family_artifact_register.UI.Util;

import com.unimelb.family_artifact_register.FoundationLayer.MapModel.MapLocation;

/**
 * activity's interface for fragment to access artifact happened location data
 */
public interface HappenedLocationListener {
    MapLocation getHappenedLocation();

    void setHappenedLocation(MapLocation happenedLocation);
}
