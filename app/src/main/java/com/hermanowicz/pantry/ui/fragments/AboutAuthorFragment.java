package com.hermanowicz.pantry.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.FragmentAboutAuthorBinding;

public class AboutAuthorFragment extends Fragment {

    private FragmentAboutAuthorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAboutAuthorBinding.inflate(inflater, container, false);

        setImageViewListeners();

        return binding.getRoot();
    }

    private void setImageViewListeners() {
        binding.linkedIn.setOnClickListener(view -> onClickLinkedIn());
        binding.facebook.setOnClickListener(view -> onClickFacebook());
    }

    private Intent prepareIntent(String websiteAddress){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(websiteAddress));
        return intent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClickLinkedIn() {
        Intent intent = prepareIntent(getString(R.string.Author_linkedin_profile));
        startActivity(intent);
    }

    public void onClickFacebook() {
        Intent intent = prepareIntent(getString(R.string.Author_facebook_profile));
        startActivity(intent);
    }
}