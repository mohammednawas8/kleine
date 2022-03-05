package com.example.kleine.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kleine.R
import com.example.kleine.databinding.FragmentOrderCompletionBinding
import com.example.kleine.util.Constants.Companion.ORDER_FAILED_FLAG
import com.example.kleine.util.Constants.Companion.ORDER_SUCCESS_FLAG
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrderCompletion : Fragment() {
    val args by navArgs<OrderCompletionArgs>()
    private lateinit var binding: FragmentOrderCompletionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderCompletionBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderFlag = args.orderCompletionFlag

        if (orderFlag == ORDER_FAILED_FLAG) {
            showErrorInformation()
            onErrorButtonClick()
        } else if (orderFlag == ORDER_SUCCESS_FLAG) {
            binding.btnCompletionAction.text = resources.getText(R.string.g_order_details)
            showSuccessInformation()
            onSuccessClick()
        }

        onCloseImageClick()
    }

    private fun onCloseImageClick() {
        binding.imgCloseOrderCompletion.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onSuccessClick() {
        binding.btnCompletionAction.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("order",args.order)
            findNavController().navigate(R.id.action_orderCompletion_to_orderDetails,bundle)
        }
    }

    private fun showSuccessInformation() {
        binding.apply {
            imgErrorTexture.setImageResource(R.drawable.payment_success)
            tvOrderFailed.text = resources.getText(R.string.g_payment_success)
            tvOrderTrack.visibility = View.VISIBLE
            tvPaymentExplanation.text =
                resources.getText(R.string.order_success_message).toString().plus(args.orderNumber)
        }
    }

    private fun onErrorButtonClick() {
        binding.btnCompletionAction.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showErrorInformation() {
        binding.apply {
            imgErrorTexture.setImageResource(R.drawable.payment_error)
            tvOrderFailed.text = resources.getText(R.string.g_payment_failed)
            tvPaymentExplanation.text = resources.getText(R.string.order_error_message)
        }
    }

    override fun onResume() {
        super.onResume()
    }

}