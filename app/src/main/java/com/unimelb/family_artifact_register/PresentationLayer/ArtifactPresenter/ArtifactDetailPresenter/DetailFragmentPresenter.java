package com.unimelb.family_artifact_register.PresentationLayer.ArtifactPresenter.ArtifactDetailPresenter;

import com.unimelb.family_artifact_register.FoundationLayer.ArtifactModel.ArtifactItem;

@Deprecated
public class DetailFragmentPresenter {

    private IView view;
    private ArtifactItem artifactItem;

    public DetailFragmentPresenter(IView view) {
        this.view = view;
        this.artifactItem = initArtifactItem();
    }

    private ArtifactItem initArtifactItem() {
        ArtifactItem res = new ArtifactItem();
        return res;
    }

    public interface IView {
        void addData(ArtifactItem artifactItem);
    }

}