package com.oguzhanaslann.fittrack.ui.termsofuse

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.databinding.FragmentTermsOfUseBinding

class TermsOfUseFragment : Fragment(R.layout.fragment_terms_of_use) {

    val binding by viewBinding(FragmentTermsOfUseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarPrivacyPolicy.setupWithNavController(navController, appBarConfiguration)
        binding.toolbarPrivacyPolicy.title = ""

        val customHtml = """
            <html>
                <body>
                    <h1>FitTrack Terms of Use</h1>
        
                    <p>Welcome to FitTrack! By using our app, you agree to be bound by these terms of use (the "Terms"). If you do not agree to these Terms, please do not use our app.</p>
        
                    <h2>1. License</h2>
        
                    <p>We grant you a limited, non-exclusive, non-transferable, revocable license to use our app for your personal, non-commercial use. You may not use our app for any other purpose without our express written consent.</p>
        
                    <h2>2. Intellectual Property</h2>
        
                    <p>Our app and all content and materials included on it, including but not limited to text, graphics, logos, images, and software, are the property of FitTrack or our licensors and are protected by copyright and trademark laws. You may not use any content or materials on our app for any commercial purpose without the express written consent of FitTrack.</p>
        
                    <h2>3. Disclaimer of Warranties</h2>
        
                    <p>Our app is provided on an "as is" and "as available" basis. We make no warranties, express or implied, as to the operation of our app or the information, content, materials, or products included on it. We will not be liable for any damages of any kind arising from the use of our app, including but not limited to direct, indirect, incidental, punitive, and consequential damages.</p>
        
                    <h2>4. Governing Law</h2>
        
                    <p>These Terms and your use of our app will be governed by and construed in accordance with the laws of the United States and the state of California, without giving effect to any principles of conflicts of law. Any disputes arising from these Terms or your use of our app will be resolved through binding arbitration in accordance with the American Arbitration Association's rules for arbitration of consumer-related disputes.</p>
        
                    <h2>5. Changes to These Terms</h2>
        
                    <p>We reserve the right to change these Terms at any time. Any changes will be effective immediately upon posting to our app. Your continued use of our app after any changes have been made will constitute your acceptance of such changes. Please review these Terms periodically for any updates or changes.</p>
        
                    <h2>6. Contact Us</h2>
        
                    <p>If you have any questions or concerns about these Terms or our app, please contact us at info@fittrackapp.com.</p>
                </body>
            </html>
        """.trimIndent()
        binding.webViewTermsOfUse.loadData(customHtml, "text/html", "UTF-8");

    }
}
