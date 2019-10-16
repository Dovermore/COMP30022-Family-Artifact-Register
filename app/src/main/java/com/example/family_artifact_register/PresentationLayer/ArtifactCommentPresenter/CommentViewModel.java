package com.example.family_artifact_register.PresentationLayer.ArtifactCommentPresenter;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.load.model.UrlUriLoader;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.Artifact;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactComment;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem;
import com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactManager;
import com.example.family_artifact_register.FoundationLayer.UserModel.UserInfo;
import com.example.family_artifact_register.FoundationLayer.UserModel.UserInfoManager;
import com.example.family_artifact_register.FoundationLayer.Util.FirebaseStorageHelper;
import com.example.family_artifact_register.PresentationLayer.ArtifactManagerPresenter.ArtifactItemWrapper;
import com.example.family_artifact_register.PresentationLayer.SocialPresenter.UserInfoWrapper;
import com.example.family_artifact_register.UI.Post.Post;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem.TAG;

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
                            CommentWrapper wrapper = new CommentWrapper(artifactComment, senderInfo);
                            wrappers.add(wrapper);
                            comments.postValue(wrappers);
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

    public LiveData<ArtifactItem> getArtifactItem(String id) {
        return artifactManager.getArtifactItemByPostId(id);
    }
}
