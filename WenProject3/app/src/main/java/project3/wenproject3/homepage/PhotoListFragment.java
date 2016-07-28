package project3.wenproject3.homepage;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
public class PhotoListFragment extends Fragment {
    private static final String TAG = "PhotoListFragment";
    private static final String KEY_USERNAME = "username";
    private String username;

    private MyDAO mDAO;
    private RecyclerView mRecyclerView;
    private PhotoListAdapter mAdapter;


    public PhotoListFragment() {
        // Required empty public constructor
    }

    public static PhotoListFragment newInstance(String username){
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME,username);
        PhotoListFragment frag = new PhotoListFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Instagram");
        mDAO = MyDAO.get(getContext());
        Bundle args = getArguments();
        username = args.getString(KEY_USERNAME);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_photo_list);
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
        User mUser = mDAO.getUser(username);
        List<String> followings = mDAO.getFollowListBySource(username);
        followings.add(username);

        List<Photo> photos = new LinkedList<>();
        String debug= "followings:";

        for (int i = 0; i < followings.size(); i++){
            List<Photo> p = mDAO.getPhotosByUsername(followings.get(i));
            debug=debug+followings.get(i);
            photos.addAll(p);

        }
        Log.i(TAG,debug);


        if(mAdapter == null) {
            mAdapter = new PhotoListAdapter(photos);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setPhotos(photos);
        }
    }



    private class PhotoListAdapter extends RecyclerView.Adapter<PhotoListHolder>{

        private List<Photo> mPhotos;

        public PhotoListAdapter (List<Photo> photos){
            mPhotos = photos;
        }

        @Override
        public PhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.view_photo_detailed, parent, false);
            return new PhotoListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PhotoListHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bind(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        public void setPhotos(List<Photo> photos){
            mPhotos = photos;
            notifyDataSetChanged();
        }
    }


    private class PhotoListHolder extends RecyclerView.ViewHolder{
        private TextView mUsername;
        private RoundImageView mProfilePic;
        private SquareImageView mImage;
        private ImageButton mLikeButton;
        private TextView mLikes;
        private Photo mPhoto;

        public PhotoListHolder(View itemView) {
            super(itemView);

            mUsername = (TextView) itemView.findViewById(R.id.view_photo_detailed_username);
            mProfilePic = (RoundImageView) itemView.findViewById(R.id.view_photo_detailed_profilepic);
            mImage = (SquareImageView) itemView.findViewById(R.id.view_photo_detailed_photo);
            mLikeButton = (ImageButton) itemView.findViewById(R.id.view_photo_detailed_like_button);
            mLikes = (TextView) itemView.findViewById(R.id.view_photo_detailed_likes);
        }

        public void bind (final Photo photo){
            mPhoto = photo;
            mUsername.setText(photo.getOwnerUsername());
            User owner = MyDAO.get(getContext()).getUser(photo.getOwnerUsername());
            File picturesDir =
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (owner.getProfilePic().equals("user")){

            }else{
                File file = new File(picturesDir, owner.getProfilePic());
                Bitmap p = PhotoUtils.getScaledBitmap(file.getPath(),
                        mProfilePic.getWidth(), mProfilePic.getHeight());
                mProfilePic.setImageBitmap(p);
            }

            File file = new File(picturesDir, photo.getFilename());
            Bitmap bitmap = PhotoUtils.getScaledBitmap(file.getPath(),
                    mImage.getWidth(), mImage.getHeight());
            mImage.setImageBitmap(bitmap);
            mLikes.setText(photo.getLikes()+"");

            List<String> likes = MyDAO.get(getContext()).getLikeListBySource(username);
            if (likes.contains(mPhoto.getId().toString())){
                mLikeButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.like_full,null));

            }

            mLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> likes = MyDAO.get(getContext()).getLikeListBySource(username);
                    if (likes.contains(mPhoto.getId().toString())){
                        Toast.makeText(getActivity(), "You already liked this photo.",Toast.LENGTH_SHORT ).show();
                    }else{
                        mLikeButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.like_full,null));
                        Like like = new Like(username,mPhoto.getId());
                        MyDAO.get(getContext()).addLike(like);
                        mPhoto.setLikes(mPhoto.getLikes()+1);
                        MyDAO.get(getContext()).updatePhoto(mPhoto);
                        updateUI();
                    }
                }
            });

        }
    }

}
