package com.unimelb.family_artifact_register.UI.Artifact.ArtifactTimeline.Util;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.family_artifact_register.PresentationLayer.Util.ArtifactItemWrapper;
import com.unimelb.family_artifact_register.R;

import java.util.List;

import static com.unimelb.family_artifact_register.UI.Util.MediaProcessHelper.TYPE_IMAGE;
import static com.unimelb.family_artifact_register.UI.Util.MediaProcessHelper.TYPE_VIDEO;
import static com.unimelb.family_artifact_register.UI.Util.MediaViewHelper.getVideoThumbNail;

/**
 * This is the Adapter class for the recyclerView in {@link TimelineRecyclerViewHelper}
 */
public class TimelineImageAdapter extends RecyclerView.Adapter<TimelineImageAdapter.TimelineImageViewHolder> {

    /**
     * classs tag
     */
    public static final String TAG = TimelineImageAdapter.class.getSimpleName();
    // data to be used
    private List<String> dataSet;
    // type of media to be displayed
    private int mediaType;

    /**
     * public constructor for instantiating a new {@link TimelineImageAdapter}
     *
     * @param wrapper the artifact item wrapper used as data source
     */
    public TimelineImageAdapter(ArtifactItemWrapper wrapper) {
        Log.d(TAG, "new recyclerview adapter");
        this.dataSet = wrapper.getLocalMediaDataUrls();
        mediaType = wrapper.getMediaType();
    }

    @NonNull
    @Override
    public TimelineImageAdapter.TimelineImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "creating each item view");
        View view = View.inflate(parent.getContext(), R.layout.timeline_item_image, null);
        return new TimelineImageAdapter.TimelineImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineImageAdapter.TimelineImageViewHolder holder, int position) {
        String url = dataSet.get(position);
        Log.d(TAG, "setting uri to image view, url: " + url);

        if (mediaType == TYPE_IMAGE) {
            holder.image.setImageURI(Uri.parse(url));
        } else if (mediaType == TYPE_VIDEO) {
            holder.image.setImageBitmap(getVideoThumbNail(url));
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     * This is the ViewHolder class for the recyclerView in {@link TimelineRecyclerViewHelper}
     */
    public class TimelineImageViewHolder extends RecyclerView.ViewHolder {

        /**
         * the {@link ImageView} in the item
         */
        public ImageView image;


        /**
         * public constructor for instantiating a new {@link TimelineImageViewHolder}
         *
         * @param itemView the inflated view for the item
         */
        public TimelineImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.timeline_item_image);
        }
    }
}