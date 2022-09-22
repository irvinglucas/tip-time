package com.portfolio.tiptime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.portfolio.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var sharedCostOfService = ""
    private var sharedTipPercentage = ""
    private var sharedRoundTip = ""
    private var sharedTipValue = ""
    private var sharedTotalCostValue = ""


    // this code is using view binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
        * here I put a logic to process if the editText is empty, so we say to the user to put some values inside*/
        binding.calculateButton.setOnClickListener {
            if (binding.costOfServiceField.text.isNullOrEmpty()) {

                Log.i(
                    "Calculate button onClickListener",
                    "THE RESULT IS: ${binding.costOfServiceField.text.isNullOrEmpty()}"
                )

                val text =
                    getString(R.string.empty_input_text_warning) // here I get the string from string.xml resource file
                Toast.makeText(this, text, Toast.LENGTH_SHORT)
                    .show()
                binding.tipValueTextview.text = ""
                binding.totalCostWithTipTextview.text = ""
            } else if (binding.costOfServiceField.text.toString() == "0") {
                val text = getString(R.string.equal_to_zero_text_warning)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            } else {
                calculateTip()
                binding.shareButton.visibility = Button.VISIBLE
            }
        }

        // create a share button
        binding.shareButton.setOnClickListener {

            /*this string has a lot of spaces because when sharing, the text is badly formatted, so I put some spaces to prettify the output*/
            val formattedShareString = """
                ${getString(R.string.share_cost_of_service_text)}: $sharedCostOfService
                ${getString(R.string.share_tip_percentage_text)}: $sharedTipPercentage
                ${getString(R.string.share_round_tip_text)}? $sharedRoundTip
                ${getString(R.string.share_tip_percentage_text)}: $sharedTipPercentage
                ${getString(R.string.share_total_cost_plus_tip_text)}: $sharedTotalCostValue
            """.trimIndent()

            val shareTotalCostIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, formattedShareString)
                type = "text/plain"
            }

            // Verify that the intent will resolve to an activity
            if (shareTotalCostIntent.resolveActivity(packageManager) != null) {
                startActivity(shareTotalCostIntent)
            }
        }
    }

    private fun calculateTip() {

        /*
        * the "binding.costOfServiceField.text" property is an 'Editable' object, so it can't be converted to double. To do so, you need to convert it to string first and then convert to double. */
        val stringInTextField = binding.costOfServiceField.text.toString()

        // here is the bill, converted to double
        val cost = stringInTextField.toDouble()

        /* I concluded that when the user changes the option, the checkedRadioButtonId number changes. So you just have to check this ID and do whatever you need to do.*/

        /* here we get the percentage and stores it in a variable with the a converted value that i'll ease the calculations*/
        val tipPercentage = when (binding.tipsOptionsGroup.checkedRadioButtonId) {
            R.id.tip_option_one -> 0.20
            R.id.tip_option_two -> 0.18
            R.id.tip_option_three -> 0.15
            else -> 0.0
        }

        // calculating the percentage
        var tip = cost * tipPercentage

        // calculating the total cost of the service. Bill  + tip on the bill
        val finalCostWithTip = when (binding.roundTipSwitch.isChecked) {
            true -> {
                tip = ceil(tip)
                cost + ceil(tip)
            }
            else -> {
                cost + tip
            }
        }

        // Updates formatted values 'Tip' and 'Total cost' to the views
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedTotalCost = NumberFormat.getCurrencyInstance().format(finalCostWithTip)

        binding.tipValueTextview.text = formattedTip
        binding.totalCostWithTipTextview.text = formattedTotalCost

        //saving all information in the outside scope to share when needed
        sharedCostOfService =
            NumberFormat.getCurrencyInstance()
                .format(binding.costOfServiceField.text.toString().toDouble())

        sharedTipPercentage = when(binding.tipsOptionsGroup.checkedRadioButtonId) {
            R.id.tip_option_one -> "20%"
            R.id.tip_option_two -> "18%"
            R.id.tip_option_three -> "15%"
            else -> "0%"
        }

        sharedRoundTip = when(binding.roundTipSwitch.isChecked) {
            true -> getString(R.string.round_tip_share_true)
            false -> getString(R.string.round_tip_share_false)
        }

        sharedTipValue = formattedTip
        sharedTotalCostValue = formattedTotalCost

    }
}