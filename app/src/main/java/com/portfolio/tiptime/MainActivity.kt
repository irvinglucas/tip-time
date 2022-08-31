package com.portfolio.tiptime

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.portfolio.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // this code is using view binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener {
            if (binding.costOfServiceField.text.isEmpty()) {
                val text = getString(R.string.empty_input_text_warning)
                Toast.makeText(this, text, Toast.LENGTH_SHORT)
                    .show()
                binding.tipValueTextview.text = ""
                binding.totalCostWithTipTextview.text = ""
            } else {
                calculateTip()
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

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedTotalCost = NumberFormat.getCurrencyInstance().format(finalCostWithTip)

//      binding.tipValueTextview.text = getString(R.string.formatted_tip ,formattedTip)
//      binding.totalCostWithTipTextview.text = getString(R.string.formatted_total_cost ,formattedTotalCost)

     binding.tipValueTextview.text = formattedTip
     binding.totalCostWithTipTextview.text = formattedTotalCost
    }
}