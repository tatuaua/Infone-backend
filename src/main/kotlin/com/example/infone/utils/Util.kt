package com.example.infone.utils

import java.text.DecimalFormat

class Util {

    companion object {
        fun formatDouble(value: Double): String {
            return DecimalFormat("#.###").format(value).replace(",", ".")
        }
    }
}