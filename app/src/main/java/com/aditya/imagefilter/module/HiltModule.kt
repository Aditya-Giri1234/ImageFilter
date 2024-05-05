package com.aditya.imagefilter.module

import androidx.fragment.app.Fragment
import com.aditya.imagefilter.repository.ImageFilterListener
import com.aditya.imagefilter.repository.SavedImageListener
import com.aditya.imagefilter.repositoryImpl.SavedImageRepoImp
import com.aditya.imagefilter.ui.fragment.EditImageFragment
import com.aditya.imagefilter.ui.fragment.Hilt_SavedImageFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent


@InstallIn(FragmentComponent::class)
@Module
 class HiltModule {

    @Provides
     fun bindImageFilterListener(editImageFragment: Fragment)= editImageFragment as ImageFilterListener
    @Provides
     fun bindSavedImageListener(savedImageFragment: Fragment)= savedImageFragment as SavedImageListener

}