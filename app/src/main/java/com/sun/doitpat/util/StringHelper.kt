package com.sun.doitpat.util

fun String.isContains(compareString: String) =
        toLowerCase().contains(compareString.toLowerCase())
