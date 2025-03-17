package com.example.infone.utils

import java.text.DecimalFormat

object Util {

    fun formatDouble(value: Double): String {
        return DecimalFormat("#.###").format(value).replace(",", ".")
    }
}