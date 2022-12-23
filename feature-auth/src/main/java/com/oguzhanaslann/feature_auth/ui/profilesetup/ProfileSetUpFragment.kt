package com.oguzhanaslann.feature_auth.ui.profilesetup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import coil.load
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showDatePicker
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentProfileSetUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ProfileSetUpFragment : Fragment(R.layout.fragment_profile_set_up) {
    private val binding by viewBinding(FragmentProfileSetUpBinding::bind)

    private val viewModel: ProfileSetUpViewModel by viewModels()

    private val navigator = Navigator()

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.onPhotoSelected(uri)
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context?.contentResolver?.takePersistableUriPermission(uri, flag)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.userProfilePhoto.observe(viewLifecycleOwner) {
            binding.profileImage.load(it) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }
        }

        lifecycleScope.launch {
            viewModel.profileSetupEvents
                .flowWithLifecycle(lifecycle)
                .collect {
                    when(it) {
                        ProfileSetupEvent.BirthdateEmpty -> showErrorSnackbar(
                            message = getString(R.string.birthdate_empty_error),
                            container = binding.root
                        )
                        ProfileSetupEvent.HeightEmpty -> showErrorSnackbar(
                            message = getString(R.string.height_empty_error),
                            container = binding.root
                        )
                        ProfileSetupEvent.NameEmpty -> showErrorSnackbar(
                            message = getString(R.string.name_empty_error),
                            container = binding.root
                        )
                        ProfileSetupEvent.SurnameEmpty -> showErrorSnackbar(
                            message = getString(R.string.surname_empty_error),
                            container = binding.root
                        )
                        ProfileSetupEvent.WeightEmpty -> showErrorSnackbar(
                            message = getString(R.string.weight_empty_error),
                            container = binding.root
                        )
                        ProfileSetupEvent.ProfileSaved -> {
                            navigator.navigateToHome(
                                navController,
                                NavOptions.Builder().setPopUpTo(R.id.nav_graph_auth, true).build()
                            )
                        }
                    }
                }
        }
    }

    private fun initViews() {
        setUpImageSelectionButton()
        setUpNameInput()
        setUpSurnameInput()
        setUpBirthdateInput()
        setUpHeightRuler()
        setUpWeightRuler()
    }

    private fun setUpImageSelectionButton() {
        binding.profileSelectPhotoButton.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        photoPicker.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private fun setUpNameInput() {
        binding.nameInputLayout.editText?.setText(viewModel.userName.value ?: "")
        binding.nameInputLayout.editText?.doAfterTextChanged {
            viewModel.setName(it?.toString() ?: "")
        }
    }

    private fun setUpSurnameInput() {
        binding.surnameInputLayout.editText?.setText(viewModel.userSurname.value ?: "")
        binding.surnameInputLayout.editText?.doAfterTextChanged {
            viewModel.setSurname(it?.toString() ?: "")
        }
    }

    private fun setUpBirthdateInput() {
        binding.dateOfBirthClickDelegate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setUpHeightRuler() {
        binding.editHeightRuler.value =
            viewModel.userHeightInCm.value ?: ProfileSetUpViewModel.DEFAULT_HEIGHT

        binding.editHeightRuler.minValue = MINIMUM_HEIGHT

        binding.editHeightRuler.setOnRulerValueChangeListener { value, displayValue ->
            viewModel.setHeight(value)
        }
    }

    private fun setUpWeightRuler() {
        binding.editWeightRuler.value =
            viewModel.userWeightInKg.value ?: ProfileSetUpViewModel.DEFAULT_WEIGHT

        binding.editWeightRuler.minValue = MINIMUM_WEIGHT

        binding.editWeightRuler.setOnRulerValueChangeListener { value, displayValue ->
            viewModel.setWeight(value)
        }
    }

    private fun showDatePickerDialog() {
        showDatePicker(
            constraintBuilder = {
                setOpenAt(Date().time)
                setEnd(Date().time)
                setValidator(DateValidatorPointBackward.now())
            },
            onDateSelected = {
                viewModel.setBirthdate(it)
            },
            datePickerBuilder = {
                setTitleText("Select Date")
            }
        )
    }

    companion object {
        const val MINIMUM_HEIGHT = 50
        const val MINIMUM_WEIGHT = 20
    }
}
