package project3.wenproject3.homepage;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.UUID;

import project3.wenproject3.R;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.SquareImageView;
import project3.wenproject3.model.Photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDialog extends DialogFragment {
    private static final String ID = "id";


    public PhotoDialog() {
        // Required empty public constructor
    }

    public static PhotoDialog newInstance (String id){
        Bundle args = new Bundle();
        args.putString(ID,id);
        PhotoDialog frag = new PhotoDialog();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_photo, null);
        Bundle args = getArguments();
        if(args != null) {
            UUID id = UUID.fromString(args.getString(ID));
            Photo photo = MyDAO.get(getContext()).getPhoto(id);
            TextView mLike = (TextView) view.findViewById(R.id.dialog_photo_like);
            SquareImageView mPhoto = (SquareImageView) view.findViewById(R.id.dialog_photo_image);

            mLike.setText(photo.getLikes()+"");

            File picturesDir =
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(picturesDir, photo.getFilename());
            Bitmap bitmap = PhotoUtils.getScaledBitmap(file.getPath(),
                    mPhoto.getWidth(), mPhoto.getHeight());
            mPhoto.setImageBitmap(bitmap);
        }
        return new AlertDialog.Builder(getContext())
                .setTitle("Photo")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
