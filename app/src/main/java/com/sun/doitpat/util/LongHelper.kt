package com.sun.doitpat.util

fun Long.isLaterThanNow(): Boolean {

    return (this - System.currentTimeMillis()) > 0
}
