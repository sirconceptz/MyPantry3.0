/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.ui.about_author;

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

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AboutAuthorFragment extends Fragment {

    private FragmentAboutAuthorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initView(inflater, container);
        setImageViewListeners();

        return binding.getRoot();
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        binding = FragmentAboutAuthorBinding.inflate(inflater, container, false);
    }

    private void setImageViewListeners() {
        binding.linkedIn.setOnClickListener(view -> onClickLinkedIn());
        binding.facebook.setOnClickListener(view -> onClickFacebook());
    }

    private Intent prepareIntent(String websiteAddress) {
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
        Intent intent = prepareIntent(getString(R.string.author_linkedin_profile));
        startActivity(intent);
    }

    public void onClickFacebook() {
        Intent intent = prepareIntent(getString(R.string.author_facebook_profile));
        startActivity(intent);
    }
}