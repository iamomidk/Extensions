package com.aperfectpolygon.extentions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M

/**
 * add Missing permission android.permission.ACCESS_NETWORK_STATE to your manifest file.
 */
@Suppress("DEPRECATION")
fun Context.isConnected(): Boolean =
	with(getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager) {
		if (SDK_INT >= M)
			with(getNetworkCapabilities(activeNetwork ?: return false) ?: return false) {
				when {
					hasTransport(TRANSPORT_WIFI) -> true
					hasTransport(TRANSPORT_CELLULAR) -> true
					hasTransport(TRANSPORT_ETHERNET) -> true
					hasTransport(TRANSPORT_BLUETOOTH) -> true
					else -> false
				}
			}
		else activeNetworkInfo?.isConnected ?: false
	}
