package project3.wenproject3.homepage;


import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import project3.wenproject3.R;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.RoundImageView;
import project3.wenproject3.model.Follow;
import project3.wenproject3.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {
    private static final String TAG = "UserListFragment";
    private static final String KEY_USERNAME = "username";
    private String username;

    private MyDAO mDAO;
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    public static UserListFragment newInstance(String username){
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME,username);
        UserListFragment frag = new UserListFragment();
        frag.setArguments(args);
        return frag;
    }

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Follows");
        mDAO = MyDAO.get(getContext());
        Bundle args = getArguments();
        username = args.getString(KEY_USERNAME);
        Log.i(TAG,username);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_user_list);
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
        List<User> users = mDAO.getUsers();
        if(mAdapter == null) {
            mAdapter = new UserListAdapter(users);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setUsers(users);
        }
    }

    private class UserListAdapter extends RecyclerView.Adapter<UserViewHolder>{
        private List<User> mUsers;

        public UserListAdapter (List<User> users){
            mUsers = users;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.view_user, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bind(user);

            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1000);
            holder.itemView.startAnimation(anim);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        public void setUsers(List<User> users){
            mUsers = users;notifyDataSetChanged();

        }
    }


    private class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView mUsername;
        private TextView mFullname;
        private RoundImageView mProfilePic;
        private Button mFollow;
        private User mUser;

        public UserViewHolder(View itemView) {
            super(itemView);

            mUsername = (TextView) itemView.findViewById(R.id.view_user_username);
            mFullname = (TextView) itemView.findViewById(R.id.view_user_fullname);
            mProfilePic = (RoundImageView) itemView.findViewById(R.id.view_user_profilepic);
            mFollow = (Button) itemView.findViewById(R.id.view_user_follow);

        }
        public void bind (final User user){
            mUser = user;
            mUsername.setText(user.getUsername());
            mFullname.setText(user.getName());

            if (mUser.getProfilePic().equals("user")){

            }else{
                File picturesDir =
                        getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(picturesDir, mUser.getProfilePic());
                Bitmap photo = PhotoUtils.getScaledBitmap(file.getPath(),
                        mProfilePic.getWidth(), mProfilePic.getHeight());
                mProfilePic.setImageBitmap(photo);
            }


            List<String> followings = MyDAO.get(getContext()).getFollowListBySource(username);

            if (followings.contains(mUser.getUsername())){
                mFollow.setText("Following");
                mFollow.setTextColor(Color.WHITE);
                mFollow.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_style4,null));

            }



            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User sourceUser = MyDAO.get(getContext()).getUser(username);
                    if(username.equals(mUser.getUsername())){
                        Toast.makeText(getActivity(), "You can't follow yourself.",Toast.LENGTH_SHORT ).show();
                    }else {
                        List<String> followings = MyDAO.get(getContext()).getFollowListBySource(username);

                        if (followings.contains(mUser.getUsername())){
                            Toast.makeText(getActivity(), "You already followed this user.",Toast.LENGTH_SHORT ).show();
                        }else{
                            mFollow.setText("Following");
                            mFollow.setTextColor(Color.WHITE);
                            mFollow.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_style4,null));
                            Follow follow = new Follow(username,user.getUsername());
                            MyDAO.get(getContext()).addFollow(follow);
                            mUser.setFollowers(mUser.getFollowers()+1);

                            sourceUser.setFollowings(sourceUser.getFollowings()+1);
                            MyDAO.get(getContext()).updateUser(mUser);
                            MyDAO.get(getContext()).updateUser(sourceUser);
                        }

                    }


                }
            });
        }

    }


}
