package com.example.mvidemo

import android.widget.Toast


fun toast(value: String) = toast { value }

inline fun toast(value: () -> String) =
    Toast.makeText(APP.context, value(), Toast.LENGTH_SHORT).show()

