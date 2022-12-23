package com.oguzhanaslann.feature_profile.ui.profileEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.oguzhanaslann.common.MINIMUM_HEIGHT
import com.oguzhanaslann.common.MINIMUM_WEIGHT
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showDatePicker
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.showSuccessSnackbar
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ProfileEditFragment : Fragment(R.layout.fragment_profile_edit) {

    private val binding by viewBinding(FragmentProfileEditBinding::bind)

    private val viewModel: ProfileEditViewModel by viewModels()
    private val args by navArgs<ProfileEditFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setProfileInformation(args.profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        subscribeObservers()

    }

    private fun initViews() {
        // setup toolbar with navigation
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setUpNameInput()
        setUpSurnameInput()
        setUpBirthdateInput()
        setUpHeightRuler()
        setUpWeightRuler()
    }

    private fun setUpNameInput() {
        binding.nameInputLayout.editText?.setText(viewModel.getName())
        binding.nameInputLayout.editText?.doAfterTextChanged {
            viewModel.setName(it?.toString() ?: "")
        }
    }

    private fun setUpSurnameInput() {
        binding.surnameInputLayout.editText?.setText(viewModel.getSurname())
        binding.surnameInputLayout.editText?.doAfterTextChanged {
            viewModel.setSurname(it?.toString() ?: "")
        }
    }

    private fun setUpBirthdateInput() {
        binding.dateOfBirthClickDelegate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        showDatePicker(
            constraintBuilder = {
                setOpenAt(viewModel.getBirthdateAsLongOrNow())
                setEnd(Date().time)
                setValidator(DateValidatorPointBackward.now())
            },
            onDateSelected = {
                viewModel.setBirthdate(it)
            },
            datePickerBuilder = {
                setTitleText(getString(com.oguzhanaslann.commonui.R.string.select_date))
            }
        )
    }

    private fun setUpHeightRuler() {
        binding.editHeightRuler.value =
            viewModel.userHeightInCm.value ?: ProfileEditViewModel.DEFAULT_HEIGHT

        binding.editHeightRuler.minValue = MINIMUM_HEIGHT

        binding.editHeightRuler.setOnRulerValueChangeListener { value, displayValue ->
            viewModel.setHeight(value)
        }
    }

    private fun setUpWeightRuler() {
        binding.editWeightRuler.value =
            viewModel.userWeightInKg.value ?: ProfileEditViewModel.DEFAULT_WEIGHT

        binding.editWeightRuler.minValue = MINIMUM_WEIGHT

        binding.editWeightRuler.setOnRulerValueChangeListener { value, displayValue ->
            viewModel.setWeight(value)
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.profileEditEvent
                .flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        ProfileEditEvent.BirthdateEmpty -> showErrorSnackbar(
                            message = getString(com.oguzhanaslann.commonui.R.string.birthdate_empty_error),
                            container = binding.root
                        )
                        ProfileEditEvent.HeightEmpty -> showErrorSnackbar(
                            message = getString(com.oguzhanaslann.commonui.R.string.height_empty_error),
                            container = binding.root
                        )
                        ProfileEditEvent.NameEmpty -> showErrorSnackbar(
                            message = getString(com.oguzhanaslann.commonui.R.string.name_empty_error),
                            container = binding.root
                        )
                        ProfileEditEvent.SurnameEmpty -> showErrorSnackbar(
                            message = getString(com.oguzhanaslann.commonui.R.string.surname_empty_error),
                            container = binding.root
                        )
                        ProfileEditEvent.WeightEmpty -> showErrorSnackbar(
                            message = getString(com.oguzhanaslann.commonui.R.string.weight_empty_error),
                            container = binding.root
                        )
                        ProfileEditEvent.ProfileSaved -> {
                            showSuccessSnackbar(
                                message = getString(R.string.profile_info_updated_description),
                                container = binding.root,
                                onDialogDismissed = { navController.popBackStack() }
                            )
                        }
                    }
                }
        }
    }
}
