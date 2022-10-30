package com.oguzhanaslann.feature_home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_home.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment(R.layout.fragment_homepage) {

    private val binding by viewBinding(FragmentHomepageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
