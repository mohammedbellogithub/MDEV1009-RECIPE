package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.recipe.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Member variables
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set content view using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Switch to HomeFragment by default
        switchFragment(new HomeFragment());

        // Bottom navigation item selection listener
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.homeNavId)
            {
                switchFragment(new HomeFragment());
            }

            else if (item.getItemId() == R.id.addNavId)
            {
                switchFragment(new AddFragment());
            }
            else if (item.getItemId() == R.id.profileNavId)
            {
                switchFragment(new ProfileFragment());
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    // Method to switch between fragments
    public void switchFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navFragmentView, fragment);
        fragmentTransaction.commit();
    }
}