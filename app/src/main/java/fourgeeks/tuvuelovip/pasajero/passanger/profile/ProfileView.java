package fourgeeks.tuvuelovip.pasajero.passanger.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.R;


import static android.app.Activity.RESULT_OK;


public class ProfileView extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = ProfileView.class.getSimpleName();

    private View view;
    private Button saveButton;
    private ImageView pictureFrame;
    private TextInputEditText lastNameText, emailText, nameText,
            passportText, phoneText;
    private ProgressBar progressBar;
    private Profile profile = new Profile();
    private TextInputEditText contactName;
    private TextInputEditText contactPhone;
    private boolean profileResponse = false;
    private boolean contactResponse = false;
    private Uri uri;
//    private String imageUrl;
//    private Uri selectedImage;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.view_profile, container, false);
        pictureFrame = (ImageView) view.findViewById(R.id.picture_frame);
        emailText = (TextInputEditText) view.findViewById(R.id.email);
        nameText = (TextInputEditText) view.findViewById(R.id.first_name);
        lastNameText = (TextInputEditText) view.findViewById(R.id.last_name);
        passportText = (TextInputEditText) view.findViewById(R.id.passport);
        phoneText = (TextInputEditText) view.findViewById(R.id.phone);
        saveButton = (Button) view.findViewById(R.id.button_save);
        contactName = (TextInputEditText) view.findViewById(R.id.contact_first_name);
        contactPhone = (TextInputEditText) view.findViewById(R.id.contact_phone);
        progressBar = (ProgressBar) view.findViewById(R.id.profile_progress);
        pictureFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        populateView();
        return view;
    }

    private void save() {
        Log.d(TAG, "save");
        String image64 = null;
        if (uri != null)
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] byteArrayImage = baos.toByteArray();
                image64 = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            } catch (IOException e) {
                Log.e(TAG, "save:" + e.getLocalizedMessage());
            }

        if (nameText.getText().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.firstname_error), Toast.LENGTH_LONG).show();
            return;
        }

        if (lastNameText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.lastname_error), Toast.LENGTH_LONG).show();
            return;
        }

        if (phoneText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            return;
        }

        if (emailText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.email_error), Toast.LENGTH_LONG).show();
            return;
        }

        // CONTACT DATA
        if (contactName.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Ingrese el nombre de la persona de contacto", Toast.LENGTH_LONG).show();
            return;
        }
        if (contactPhone.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Ingrese el número de teléfono de la persona de contacto", Toast.LENGTH_LONG).show();
            return;
        }

//        //END CONTACT DATA
//
//        if (selectedImage != null) {
//            checkReadPermission();
//        }

        profile.firstName = nameText.getText().toString();
        profile.lastName = lastNameText.getText().toString();
        profile.contactPhone = phoneText.getText().toString();
        profile.email = emailText.getText().toString();
        profile.passport = passportText.getText().toString();
        profile.image = image64;

        progressBar.setVisibility(View.VISIBLE);
        ProfileModel.saveProfile(profile, new ApiResponseListener<Profile>() {
            @Override
            public void onResponse(Profile result) {
                Log.d(TAG, "save:onResponse:" + result);
                saveContact();
            }

            @Override
            public void onError(VolleyError error, String message) {
                Log.e(TAG, "save:onError:" + message);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void saveContact() {
        Log.e(TAG, "saveContact");
        Profile contact = new Profile();
        contact.firstName = contactName.getText().toString();
        contact.contactPhone = contactPhone.getText().toString();

        ProfileModel.saveContactProfile(profile, contact, new ApiResponseListener<Profile>() {
            @Override
            public void onResponse(Profile result) {
                Log.e(TAG, "saveConurtact:onResponse:" + result);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.profile_changed), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VolleyError error, String message) {
                Log.e(TAG, "saveContact:onError:" + message);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void gallery() {
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.with(getActivity()).load(uri).transform(new CircleTransform()).into(pictureFrame);
        }
//        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//                    selectedImage = data.getData();
//                    Picasso.with(getActivity()).load(selectedImage).transform(new CircleTransform()).into(pictureFrame);
//                }
//
//                break;
//
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    selectedImage = data.getData();
//                    Picasso.with(getActivity()).load(selectedImage).transform(new CircleTransform()).into(pictureFrame);
//                }
//                break;
//        }
//
//        if (selectedImage != null) {
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            if (cursor.moveToFirst()) {
//                imageUrl = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//            }
//            cursor.close();
//        }
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    private void populateView() {
        Log.d(TAG, "getData");
        progressBar.setVisibility(View.VISIBLE);
        ProfileModel.getProfile(new ApiResponseListener<Profile>() {
            @Override
            public void onResponse(Profile result) {
                Log.d(TAG, "getData:onResponse:" + result);
                profile.pasteProfile(result);
                populateProfile(result);
                profileResponse = true;

                ProfileModel.getContactProfile(new ApiResponseListener<Profile>() {
                    @Override
                    public void onResponse(Profile result) {
                        populateContactProfile(result);
                        contactResponse = true;
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(VolleyError error, String message) {
                        contactResponse = true;
                        progressBar.setVisibility(View.GONE);
                    }
                }, profile);
            }

            @Override
            public void onError(VolleyError error, String message) {
                Log.e(TAG, "getData:onError:" + message);
                profileResponse = true;
                hideProgress();
            }
        });

    }

    private void populateProfile(Profile profile) {
        if (profile.image != null) {
            if (!profile.image.isEmpty() && profile.image != "null") {
//                Picasso.with(getActivity()).load(uri).transform(new CircleTransform()).into(pictureFrame);
                pictureFrame.setImageBitmap(new CircleTransform().transform(Util.decodeFromBase64(profile.image)));
            }
        }

//        if (profile.image != null) {
//            if (!profile.image.isEmpty() && profile.image != "null") {
//                pictureFrame.setImageBitmap(Util.decodeFromBase64(profile.image));
//            }
//
//        }

        if (profile.firstName != null) {
            if (!profile.firstName.equals("null")) {
                nameText.setText(profile.firstName);
            }
        }

        if (profile.lastName != null) {
            if (!profile.lastName.equals("null")) {
                lastNameText.setText(profile.lastName);
            }
        }

        if (profile.contactPhone != null) {
            if (!profile.contactPhone.equals("null")) {
                phoneText.setText(profile.contactPhone);
            }

        }

        if (profile.email != null) {
            if (!profile.email.equals("null")) {
                emailText.setText(profile.email);
            }

        }

        if (profile.passport != null) {
            if (!profile.passport.equals("null")) {
                passportText.setText(profile.passport);
            }

        }

        this.profile = profile;
    }

    private void populateContactProfile(Profile profile) {
        if (profile.firstName != null) {
            if (!profile.firstName.equals("null")) {
                contactName.setText(profile.firstName);
            }

            if (profile.contactPhone != null) {
                if (!profile.contactPhone.equals("null")) {
                    contactPhone.setText(profile.contactPhone);
                }
            }

            if (profile.email != null) {
                if (!profile.email.equals("null")) {
                    nameText.setText(profile.email);
                }
            }
        }
    }

    private void hideProgress() {
        if (profileResponse && contactResponse) {
            progressBar.setVisibility(View.GONE);
            profileResponse = false;
            contactResponse = false;
        }
    }

//    private void checkReadPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
//            convertToBase64();
//            return;
//        }
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        }
//
//    }

//    private void convertToBase64() {
//        File file = new File(imageUrl);
//
//        if (file.exists()) {
//            if (file.length() > 2024000) {
//                Toast.makeText(getActivity(), "Archivo de Imagen Demasiado Grande", Toast.LENGTH_LONG).show();
//                return;
//            }
//            Bitmap bm = BitmapFactory.decodeFile(imageUrl);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//            byte[] byteArrayImage = baos.toByteArray();
//
//            profile.image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
//        }
//    }

//    private Bitmap decodeFromBase64() {
//        byte[] decodedString = Base64.decode(profile.image, Base64.DEFAULT);
//
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//        return decodedByte;
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 0: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    convertToBase64();
//
//                } else {
//
//
//                }
//                return;
//            }
//
//        }
//    }

}



