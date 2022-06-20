package com.jonathas.petclinic.ui.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jonathas.petclinic.databinding.FragmentWebContentBinding

class WebContentFragment : Fragment() {

    private val fragmentArgs: WebContentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentWebContentBinding.inflate(inflater, container, false).apply {
            webContent.apply {
                webViewClient = WebViewClient()
                loadUrl(fragmentArgs.url)
            }
        }.root

}