package com.example.family_artifact_register.PresentationLayer.HubPresenter;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.example.family_artifact_register.FoundationLayer.ArtifactModel.Artifact;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactManager;
import com.example.family_artifact_register.FoundationLayer.UserModel.UserInfo;
import com.example.family_artifact_register.FoundationLayer.UserModel.UserInfoManager;
import com.example.family_artifact_register.FoundationLayer.Util.FirebaseStorageHelper;
import com.example.family_artifact_register.PresentationLayer.ArtifactManagerPresenter.ArtifactItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HubViewModel extends AndroidViewModel {

    public static final String TAG = HubViewModel.class.getSimpleName();

    private UserInfoManager userInfoManager = UserInfoManager.getInstance();

    private ArtifactManager artifactManager = ArtifactManager.getInstance();

    private FirebaseStorageHelper fSHelper = FirebaseStorageHelper.getInstance();

    private MutableLiveData<List<ArtifactItem>> artifactList;

    private MutableLiveData<List<ArtifactItemWrapper>> artifactWrapperList;

    private String currentUid;

    public HubViewModel(Application application) {
        super(application);
        getPostsChange();
    }

    public void getPostsChange() {
        currentUid = userInfoManager.getCurrentUid();
        artifactList = (MutableLiveData<List<ArtifactItem>>) artifactManager.getArtifactItemByUid(currentUid);
        artifactWrapperList = new MutableLiveData<>();
        ArrayList<ArtifactItemWrapper> wrappers = new ArrayList<>();
        artifactWrapperList.setValue(wrappers);
        artifactList.observeForever(new Observer<List<ArtifactItem>>() {
            @Override
            public void onChanged(List<ArtifactItem> artifactItems) {
                for(ArtifactItem item: artifactItems) {
                    List<String> mediaDataRemoteUrls = item.getMediaDataUrls();
                    ArtifactItemWrapper wrapper = new ArtifactItemWrapper(item);

                    fSHelper.loadByRemoteUri(mediaDataRemoteUrls).observeForever(new Observer<List<Uri>>() {
                        @Override
                        public void onChanged(List<Uri> uris) {
                            Log.d(TAG, "local uris: " + uris.toString());

                            // load data to wrapper
                            wrapper.setLocalMediaDataUrls(
                                    uris.stream()
                                            .map(Objects::toString)
                                            .collect(Collectors.toList())
                            );

                            wrappers.add(wrapper);
                            artifactWrapperList.setValue(wrappers);
                            // artifactList.setValue(artifactList.getValue());
                        }
                    });
                }
            }
        });
    }

    public LiveData<List<ArtifactItemWrapper>> getPosts() {
        return artifactWrapperList;
    }

}
