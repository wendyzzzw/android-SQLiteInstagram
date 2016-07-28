package project3.wenproject3.homepage;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project3.wenproject3.R;
import project3.wenproject3.SignUpActivity;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.RoundImageView;
import project3.wenproject3.image.SquareImageView;
import project3.wenproject3.model.Photo;
import project3.wenproject3.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    public static final String KEY_USERNAME = "username";
    private String username;
    private MyDAO mDAO;
    private User mUser;

    private TextView mPosts;
    private TextView mFollowers;
    private TextView mFollowing;
    private TextView mFullName;
    private RoundImageView mProfilePic;


    private RecyclerView mRecyclerView;
    private List<File> mFileList;
    private PhotoAdapter mPhotoAdapter;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance (String username){
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME,username);
        UserProfileFragment frag = new UserProfileFragment();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        username = args.getString(KEY_USERNAME);
        mDAO = MyDAO.get(getContext());
        mUser = mDAO.getUser(username);
        getActivity().setTitle(username);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mPosts = (TextView) view.findViewById(R.id.up_posts);
        mFollowers = (TextView) view.findViewById(R.id.up_followers);
        mFollowing = (TextView) view.findViewById(R.id.up_following);
        mProfilePic = (RoundImageView) view.findViewById(R.id.up_pic);
        mFullName = (TextView) view.findViewById(R.id.up_full_name);

        mPosts.setText(""+mUser.getPosts());
        mFollowers.setText(""+mUser.getFollowers());
        mFollowing.setText(""+mUser.getFollowings());
        mFullName.setText(mUser.getName());

        if (mUser.getProfilePic().equals("user")){

        }else{
            File picturesDir =
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(picturesDir, mUser.getProfilePic());
            Bitmap photo = PhotoUtils.getScaledBitmap(file.getPath(),
                    mProfilePic.getWidth(), mProfilePic.getHeight());
            mProfilePic.setImageBitmap(photo);
        }

        TextView mEditProfile = (TextView) view.findViewById(R.id.up_edit_profile);
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                intent.putExtra(KEY_USERNAME,username);
                startActivityForResult(intent,0);
            }
        });


        mFileList = new ArrayList<>();
        List<Photo> mPhotos =MyDAO.get(getContext()).getPhotosByUsername(username);


        mRecyclerView = (RecyclerView)view.findViewById(R.id.up_recycler_container);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mPhotoAdapter = new PhotoAdapter(mPhotos);
        mRecyclerView.setAdapter(mPhotoAdapter);
        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        List<Photo> mPhotos =MyDAO.get(getContext()).getPhotosByUsername(username);


        if(mPhotoAdapter == null) {
            mPhotoAdapter = new PhotoAdapter(mPhotos);
            mRecyclerView.setAdapter(mPhotoAdapter);
        }
        else {
            mPhotoAdapter.setPhotoFiles(mPhotos);
        }
    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<Photo> mPhotos;

        PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            SquareImageView view = (SquareImageView) inflater.inflate(R.layout.view_photo, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            holder.bind(mPhotos.get(position));
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        public void setPhotoFiles(List<Photo> photos) {
            mPhotos=photos;
            notifyDataSetChanged();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private SquareImageView mImageView;
        private Photo mPhoto;

        public PhotoHolder(SquareImageView itemView) {
            super(itemView);
            mImageView = itemView;
            mImageView.setOnClickListener(this);
        }

        public void bind(Photo photo) {
            mPhoto = photo;
            File picturesDir =
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(picturesDir, photo.getFilename());
            Bitmap bitmap = PhotoUtils.getScaledBitmap(file.getPath(),
                    mImageView.getWidth(), mImageView.getHeight());
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            AppCompatActivity c = (AppCompatActivity)v.getContext();
            FragmentManager manager = c.getSupportFragmentManager();
            PhotoDialog dialog = PhotoDialog.newInstance(mPhoto.getId().toString());
            dialog.show(manager, "Photo Dialog");
        }
    }
}
