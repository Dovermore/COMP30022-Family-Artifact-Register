package com.example.family_artifact_register.FoundationLayer.ArtifactModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @description artifact data type for timelined artifact i.e.: multiple artifact indexed by time
 */
public class ArtifactTimeline extends Artifact {

    /**
     * family artifacts stores in the timeline
     */
    private List<String> artifactItemPostIds;

    /**
     * the title for the timeline which describe about the timeline
     */
    private String title;

    public ArtifactTimeline(String postId, String uid, String uploadDateTime,
                            String lastUpdateDateTime, List<String> artifactItemPostIds,
                            String title) {
        super(postId, uid, uploadDateTime, lastUpdateDateTime);
        this.artifactItemPostIds = artifactItemPostIds;
        this.title = title;
    }

    public ArtifactTimeline(String title) {
        super();

        this.title = title;
        artifactItemPostIds = new ArrayList<>();
    }

    public void addArtifact(ArtifactItem artifactItem) {
        artifactItemPostIds.add(artifactItem.getPostId());
    }

    public void removeArtifact(ArtifactItem artifactItem) {
        artifactItemPostIds.remove(artifactItem.getPostId());
    }

    /**
     * @return the string for the title
     */
    public String getTitle() { return this.title; }

    @Override
    public String getPostId() {
        return super.getPostId();
    }

    @Override
    public String getUid() {
        return super.getUid();
    }

    @Override
    public String getUploadDateTime() {
        return super.getUploadDateTime();
    }

    @Override
    public String getLastUpdateDateTime() {
        return super.getLastUpdateDateTime();
    }
}
