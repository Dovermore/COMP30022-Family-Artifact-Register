package com.unimelb.family_artifact_register.PresentationLayer.ArtifactCommentPresenter;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.unimelb.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactComment;
import com.unimelb.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem;
import com.unimelb.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactManager;
import com.unimelb.family_artifact_register.FoundationLayer.UserModel.UserInfo;
import com.unimelb.family_artifact_register.FoundationLayer.UserModel.UserInfoManager;
import com.unimelb.family_artifact_register.FoundationLayer.Util.FirebaseStorageHelper;
import com.unimelb.family_artifact_register.PresentationLayer.Util.UserInfoWrapper;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel extends AndroidViewModel {

    public static final String TAG = CommentViewModel.class.getSimpleName();

    private UserInfoManager userInfoManager = UserInfoManager.getInstance();
    private ArtifactManager artifactManager = ArtifactManager.getInstance();
    private FirebaseStorageHelper fSHelper = FirebaseStorageHelper.getInstance();

    private String PostID;
    private LiveData<ArtifactItem> artifact;
    private List<CommentWrapper> commentList = new ArrayList<>();
    private MutableLiveData<List<CommentWrapper>> comments = new MutableLiveData<>();

    public CommentViewModel(@NonNull Application application, String PostId) {
        super(application);
        this.PostID = PostId;
        comments.postValue(commentList);
        getComment();
    }

    public void getComment() {
        // now processing this user's artifact info
        artifactManager.listenCommentByArtifact(PostID, "CommentViewModel1").observeForever(new Observer<List<ArtifactComment>>() {
            @Override
            public void onChanged(List<ArtifactComment> artifactComments) {
                List<CommentWrapper> wrappers = new ArrayList<>();
                comments.setValue(wrappers);
                for(ArtifactComment artifactComment: artifactComments) {
                    Log.d(TAG, "retrieved comment data about user with comment: " + artifactComment.getContent());
                    userInfoManager.listenUserInfo(artifactComment.getUid()).observeForever(new Observer<UserInfo>() {
                        @Override
                        public void onChanged(UserInfo userInfo) {
                            Log.d(TAG, "retrieved comment data about user with uid " + userInfo.getUid());
                            UserInfoWrapper senderInfo = new UserInfoWrapper(userInfo);
                            String url = senderInfo.getPhotoUrl();
                            if(url == null) {
                                senderInfo.setPhotoUrl(null);
                                CommentWrapper wrapper = new CommentWrapper(artifactComment, senderInfo);
                                wrappers.add(wrapper);
                                comments.postValue(wrappers);
                            }
                            else {
                                fSHelper.loadByRemoteUri(url).observeForever(new Observer<Uri>() {
                                    @Override
                                    public void onChanged(Uri uri) {
                                        Log.d(TAG, "photo uri come back from DB: " + uri.toString());
                                        senderInfo.setPhotoUrl(uri.toString());
                                        CommentWrapper wrapper = new CommentWrapper(artifactComment, senderInfo);
                                        wrappers.add(wrapper);
                                        comments.postValue(wrappers);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    public LiveData<List<CommentWrapper>> getComments() {
        return comments;
    }


    public void addComment(String comment) {
        artifactManager.addComment(PostID, userInfoManager.getCurrentUid(), comment);
    }

    @Deprecated
    public LiveData<ArtifactItem> getArtifactItem(String id) {
        return artifactManager.getArtifactItemByPostId(id);
    }

    public LiveData<UserInfoWrapper> getCurrentUserInfo() {
        MutableLiveData<UserInfoWrapper> me = new MutableLiveData<>();
        userInfoManager.listenUserInfo(userInfoManager.getCurrentUid()).observeForever(new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                UserInfoWrapper wrapper = new UserInfoWrapper(userInfo);
                if(wrapper.getPhotoUrl() == null) {
                    wrapper.setPhotoUrl(null);
                    me.postValue(wrapper);
                }
                else {
                    fSHelper.loadByRemoteUri(wrapper.getPhotoUrl()).observeForever(new Observer<Uri>() {
                        @Override
                        public void onChanged(Uri uri) {
                            wrapper.setPhotoUrl(uri.toString());
                            me.postValue(wrapper);
                        }
                    });
                }

            }
        });
        return me;
    }
}
