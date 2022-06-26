package com.aperfectpolygon.extentions

import android.net.Uri


fun Uri.addUriParameter(key: String, newValue: String): Uri = buildUpon().clearQuery().apply {
	var isSameParamPresent = false
	queryParameterNames.forEach { param ->
		// if same param is present override it, otherwise add the old param back
		appendQueryParameter(
			param, when (param) {
				key -> newValue
				else -> getQueryParameter(param)
			}
		)
		// make sure we do not add new param again if already overridden
		if (param == key) isSameParamPresent = true
	}
	// never overrode same param so add new passed value now
	if (!isSameParamPresent) appendQueryParameter(key, newValue)
}.build()

fun Uri.addUriParameter(vararg pairs: Pair<String, String?>): Uri = buildUpon().clearQuery().apply {
	pairs.forEach { (key, newValue) ->
		var isSameParamPresent = false
		queryParameterNames.forEach { param ->
			// if same param is present override it, otherwise add the old param back
			appendQueryParameter(
				param, when (param) {
					key -> newValue
					else -> getQueryParameter(param)
				}
			)
			// make sure we do not add new param again if already overridden
			if (param == key) isSameParamPresent = true
		}
		// never overrode same param so add new passed value now
		if (!isSameParamPresent) appendQueryParameter(key, newValue)
	}
}.build()
