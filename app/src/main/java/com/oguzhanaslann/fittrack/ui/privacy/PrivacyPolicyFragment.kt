package com.oguzhanaslann.fittrack.ui.privacy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : Fragment(R.layout.fragment_privacy_policy) {

    private val binding by viewBinding(FragmentPrivacyPolicyBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarTermsOfUse.setupWithNavController(navController, appBarConfiguration)
        binding.toolbarTermsOfUse.title = ""

        val customHtml = """
            <html>
                <body>
                  <h1>FitTrack Privacy Policy</h1>
                
                  <p>At FitTrack, we are committed to protecting your privacy. This privacy policy (the "Policy") explains how we collect, use, and share information about you when you use our app and our website (collectively, the "Services"). By using our Services, you agree to the collection, use, and sharing of your information as described in this Policy. If you do not agree with our policies and practices, do not use our Services.</p>
                
                  <h2>1. Information We Collect</h2>
                
                  <p>We collect information about you when you use our Services, including:</p>
                  <ul>
                    <li>Information you provide to us directly, such as when you create an account or profile, make a purchase, or communicate with us.</li>
                    <li>Information we collect automatically when you use our Services, such as your IP address, device type, and usage data.</li>
                    <li>Information we receive from third parties, such as social media platforms when you connect your account to our Services.</li>
                  </ul>
                
                  <h2>2. How We Use Your Information</h2>
                
                  <p>We use the information we collect about you to provide and improve our Services, to communicate with you, and to comply with legal obligations. Specifically, we use your information to:</p>
                  <ul>
                    <li>Provide and deliver the products and services you request, process transactions, and send you related information, such as purchase confirmations and invoices.</li>
                    <li>Communicate with you about your account and our Services, including by sending you alerts, updates, and other notices.</li>
                    <li>Personalize your experience by presenting products and offers tailored to you.</li>
                    <li>Improve and optimize our Services by analyzing how you use them.</li>
                    <li>Comply with legal obligations, such as responding to legal process or enforcing our policies.</li>
                  </ul>
                
                  <h2>3. Sharing Your Information</h2>
                
                  <p>We may share your information with third parties in the following circumstances:</p>
                  <ul>
                    <li>With vendors, consultants, and other service providers who need access to your information to perform services on our behalf.</li>
                    <li>With business partners, such as when you make a purchase through our Services.</li>
                    <li>In connection with a merger, acquisition, bankruptcy, or other sale of all or a portion of our assets.</li>
                    <li>To comply with legal obligations, such as in response to a subpoena or court order.</li>
                    <li>To enforce our policies or protect the rights, property, or safety of FitTrack, our users, or others.</li>
                  </ul>
                
                  <h2>4. Your Choices</h2>
                
                  <p>You can control the information you share with us by updating your account settings or by contacting us at info@fittrackapp.com. You can also opt out of receiving marketing communications from us by following the unsubscribe instructions in those communications.</p>
                
                  <h2>5. Data Retention and Security</h2>
                
                  <p>We retain your information for as long as necessary to provide our Services, comply with legal obligations,resolve disputes, and enforce our agreements. We take reasonable measures to protect your information from unauthorized access, use, or disclosure. However, no internet transmission is completely secure, and we cannot guarantee the security of your information. You should take care when using the internet and our Services, and make sure you protect your login information and devices.</p>

                <h2>6. Children's Privacy</h2>
            
                <p>Our Services are not intended for children under the age of 13. We do not knowingly collect personal information from children under 13. If you are a parent or guardian and you believe that your child under 13 has provided us with personal information, please contact us at info@fittrackapp.com. We will take steps to delete such information from our files.</p>
            
                <h2>7. International Transfer</h2>
            
                <p>Your information may be transferred to, and processed in, countries other than the country in which you are resident. These countries may have data protection laws that are different from the laws of your country. We will take steps to ensure that your information is treated securely and in accordance with this Policy. By using our Services, you agree to the transfer of your information to these countries.</p>
            
                <h2>8. Changes to This Policy</h2>
            
                <p>We may update this Policy from time to time. We will notify you of any changes by posting the new Policy on this page. You are advised to review this Policy periodically for any changes. Changes are effective immediately when we post them. By continuing to use our Services after those changes become effective, you agree to the revised Policy.</p>
            
                <h2>9. Contact Us</h2>
            
                <p>If you have any questions about this Policy, please contact us at info@fittrackapp.com.</p>
              </body>
            </html>
        """.trimIndent()
        binding.webViewPrivacyPolicy.loadData(customHtml, "text/html", "UTF-8");

    }
}
