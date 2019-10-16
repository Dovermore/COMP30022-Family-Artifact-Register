package com.example.family_artifact_register.PresentationLayer.MapPresenter;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactManager;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactTimeline;
import com.example.family_artifact_register.FoundationLayer.MapModel.MapLocation;
import com.example.family_artifact_register.FoundationLayer.MapModel.MapLocationManager;
import com.example.family_artifact_register.FoundationLayer.UserModel.UserInfoManager;
import com.example.family_artifact_register.FoundationLayer.Util.FirebaseStorageHelper;
import com.example.family_artifact_register.PresentationLayer.ArtifactManagerPresenter.ArtifactItemWrapper;
import com.example.family_artifact_register.PresentationLayer.Util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapStoredViewModel extends AndroidViewModel {

    public static final String TAG = MapStoredViewModel.class.getSimpleName();

    private UserInfoManager userInfoManager = UserInfoManager.getInstance();
    private ArtifactManager artifactManager = ArtifactManager.getInstance();
    private MapLocationManager mapLocationManager = MapLocationManager.getInstance();
    private FirebaseStorageHelper helper = FirebaseStorageHelper.getInstance();

    private LiveData<List<ArtifactItem>> artifacts;

    private List<MapLocation> mapLocations = new ArrayList<>();

    private MediatorLiveData<List<MapLocation>> locations = new MediatorLiveData<>();

    private MutableLiveData<List<TimelineMapWrapper>> timelineWrappers = new MutableLiveData<>();

    public MapStoredViewModel(@NonNull Application application) {
        super(application);
        artifacts = artifactManager.getArtifactItemByUid(userInfoManager.getCurrentUid());
        locations.setValue(mapLocations);
    }

    public LiveData<List<TimelineMapWrapper>> getMapWrapper() {

        artifactManager.listenArtifactTimelineByUid(userInfoManager.getCurrentUid(), "MapStoredViewModel1").observeForever(new Observer<List<ArtifactTimeline>>() {
            @Override
            public void onChanged(List<ArtifactTimeline> artifactTimelines) {
                Log.d(TAG, "retrieved all timeline data for current user");
                List<TimelineMapWrapper> wrappers = new ArrayList<>();
                timelineWrappers.postValue(wrappers);

                for(ArtifactTimeline timeline: artifactTimelines) {
//                    List<ArtifactItemWrapper> itemWrappers = new ArrayList<>();
//                    List<MapLocation> mapLocations = new ArrayList<>();
//                    TimelineMapWrapper timelineWrapper = new TimelineMapWrapper(timeline, itemWrappers, mapLocations);
//                    wrappers.add(timelineWrapper);
//                    timelineWrappers.postValue(wrappers);
                    List<Pair<ArtifactItemWrapper, MapLocation>> pairs = new ArrayList<>();
                    TimelineMapWrapper timelineWrapper = new TimelineMapWrapper(timeline, pairs);

                    List<String> itemIDs = timeline.getArtifactItemPostIds();
                    for(String id: itemIDs) {

                        artifactManager.listenArtifactItemByPostId(id, "MapStoredViewModel2").observeForever(new Observer<ArtifactItem>() {
                            @Override
                            public void onChanged(ArtifactItem artifactItem) {
                                Log.d(TAG, "retrieved data about artifact item with id: " + artifactItem.getPostId());

                                ArtifactItemWrapper wrapper = new ArtifactItemWrapper(artifactItem);
                                timelineWrappers.postValue(wrappers);
                                helper.loadByRemoteUri(artifactItem.getMediaDataUrls()).observeForever(new Observer<List<Uri>>() {
                                    @Override
                                    public void onChanged(List<Uri> uris) {
                                        wrapper.setLocalMediaDataUrls(
                                                uris.stream()
                                                        .map(Objects::toString)
                                                        .collect(Collectors.toCollection(ArrayList::new))
                                        );

                                        mapLocationManager.getMapLocationById(artifactItem.getLocationStoredId()).observeForever(new Observer<MapLocation>() {
                                            @Override
                                            public void onChanged(MapLocation mapLocation) {
//                                        mapLocations.add(mapLocation);
                                                timelineWrapper.getAllPairs().add(new Pair<>(wrapper, mapLocation));
//                                        wrappers.add(timelineWrapper);
                                                timelineWrappers.postValue(wrappers);
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
//                    TimelineMapWrapper timelineWrapper = new TimelineMapWrapper(timeline, itemWrappers, mapLocations);
                    wrappers.add(timelineWrapper);
                    timelineWrappers.postValue(wrappers);
                }
            }
        });
        return timelineWrappers;
    }
}
