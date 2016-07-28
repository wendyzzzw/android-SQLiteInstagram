package project3.wenproject3.homepage;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import project3.wenproject3.R;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.RoundImageView;
import project3.wenproject3.image.SquareImageView;
import project3.wenproject3.model.Like;
import project3.wenproject3.model.Photo;
import project3.wenproject3.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeListFragment extends Fragment {
    private static final String KEY_USERNAME = "username";
    private String username;

    private MyDAO mDAO;
    private RecyclerView mRecyclerView;
    private LikeListAdapter mAdapter;

    int lastPosition = -1;

    public LikeListFragment() {
        // Required empty public constructor
    }

    public static LikeListFragment newInstance(String username){
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME,username);
        LikeListFragment frag = new LikeListFragment();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Likes");
        mDAO = MyDAO.get(getContext());
        Bundle args = getArguments();
        username = args.getString(KEY_USERNAME);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_like_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        List<Photo> photos = mDAO.getPhotosByUsername(username);
        List<Like> likes = new LinkedList<>();

        for (int i=0; i<photos.size(); i++){
            List<Like> l = mDAO.getLikeListByDestination(photos.get(i).getId());
            likes.addAll(l);
        }


        if(mAdapter == null) {
            mAdapter = new LikeListAdapter(likes);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setLikes(likes);
        }
    }




    private class LikeListAdapter extends RecyclerView.Adapter<LikeListHolder>{
        private List<Like> mLikes;

        public LikeListAdapter (List<Like> likes){
            mLikes = likes;
        }

        @Override
        public LikeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.view_like, parent, false);
            return new LikeListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LikeListHolder holder, int position) {
            Like like = mLikes.get(position);
            holder.bind(like);

            if (position > lastPosition)
            {
                AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000);
                holder.itemView.startAnimation(anim);
                lastPosition = position;
            }


        }

        @Override
        public int getItemCount() {
            return mLikes.size();
        }

        public void setLikes(List<Like> likes){
            mLikes = likes;
            notifyDataSetChanged();
        }
    }

    private class LikeListHolder extends RecyclerView.ViewHolder{
        private Like mLike;
        private RoundImageView mProfilePic;
        private TextView mUsername;
        private SquareImageView mPhoto;

        public LikeListHolder(View itemView) {
            super(itemView);
            mProfilePic = (RoundImageView)itemView.findViewById(R.id.view_like_profilepic);
            mUsername = (TextView) itemView.findViewById(R.id.view_like_username);
            mPhoto = (SquareImageView)itemView.findViewById(R.id.view_like_photo);
        }

        public void bind (final Like like){
            mLike = like;
            String username = mLike.getSourceUsername();
            User user = MyDAO.get(getContext()).getUser(username);

            mUsername.setText(username);
            File picturesDir =
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (user.getProfilePic().equals("user")){

            }else{
                File file = new File(picturesDir, user.getProfilePic());
                Bitmap p = PhotoUtils.getScaledBitmap(file.getPath(),
                        mProfilePic.getWidth(), mProfilePic.getHeight());
                mProfilePic.setImageBitmap(p);
            }

            Photo photo = MyDAO.get(getContext()).getPhoto(mLike.getDestinationPhotoId());
            File file = new File(picturesDir, photo.getFilename());
            Bitmap bitmap = PhotoUtils.getScaledBitmap(file.getPath(),
                    mPhoto.getWidth(), mPhoto.getHeight());
            mPhoto.setImageBitmap(bitmap);
        }


    }

}
