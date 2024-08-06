package com.example.recipe;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;

    private EditText editTextName, editTextCategory, editTextDate, editTextDirections, editTextIngredients;
    private ImageView imageView;
    private Button buttonAddRecipe, buttonPickImage, buttonCaptureImage, backButton;
    private Uri imageUri;
    private FirebaseFirestore firestore;

    private String CHANNEL_ID;
    private int notification_Id = 1;

    public AddFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize views
        editTextName = view.findViewById(R.id.edit_text_name);
        editTextCategory = view.findViewById(R.id.edit_text_category);
        editTextDate = view.findViewById(R.id.edit_text_date);
        editTextDirections = view.findViewById(R.id.edit_text_directions);
        editTextIngredients = view.findViewById(R.id.edit_text_ingredients);
        imageView = view.findViewById(R.id.image_view);
        buttonAddRecipe = view.findViewById(R.id.button_add_recipe);
        buttonPickImage = view.findViewById(R.id.button_pick_image);
        buttonCaptureImage = view.findViewById(R.id.button_capture_image);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();

        CHANNEL_ID = getString(R.string.channel_id);

        createNotificationChannel();
        requestNotificationPermission();

        // Request necessary permissions
        requestPermissions();

        // Set click listener for the pick image button
        buttonPickImage.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .galleryOnly()
                .start());

        // Set click listener for the capture image button
        buttonCaptureImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
            } else {
                captureImage();
            }
        });

        // Set click listener for the add button
        buttonAddRecipe.setOnClickListener(v -> addRecipe());
        // Find views

        backButton = view.findViewById(R.id.back_btn);

        backButton.setOnClickListener(v -> {
            navigateBack();
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void captureImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.CAMERA
            }, REQUEST_CODE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void addRecipe() {
        // Get input values
        String name = editTextName.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String directions = editTextDirections.getText().toString().trim();
        String ingredients = editTextIngredients.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(date) || TextUtils.isEmpty(directions) || TextUtils.isEmpty(ingredients) || imageUri == null) {
            Toast.makeText(getContext(), "Please fill all fields and pick an image", Toast.LENGTH_SHORT).show();
            return;
        }
        saveRecipeToFirestore(name, category, date, directions, ingredients, "https://res.cloudinary.com/xclusivetech/image/upload/v1722967575/ps5npitpnsjyp4cm7kdy.webp");
    }

    private void saveRecipeToFirestore(String name, String category, String date, String directions, String ingredients, String imageUrl) {
        // Create a new recipe object
        RecipeModel recipe = new RecipeModel(name, category, date, directions, ingredients, imageUrl);

        // Save to Firestore
        firestore.collection("recipes")
                .add(recipe)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Recipe added", Toast.LENGTH_SHORT).show();
                    requestNotification();
                    navigateBack();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding recipe", Toast.LENGTH_SHORT).show());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Request notification method
    private void requestNotification() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // A toast alert if the notification request was denied initially
            Toast.makeText(requireContext(), getString(R.string.notification_denied), Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerComp = NotificationManagerCompat.from(requireContext());
        notificationManagerComp.notify(notification_Id++, builder.build());
    }

    // Request notification permission
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void navigateBack() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navFragmentView, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
