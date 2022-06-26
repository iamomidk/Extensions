package com.aperfectpolygon.extentions

val CharSequence.isEmpty: Boolean
	get() = isEmpty()
val CharSequence.isNotEmpty: Boolean
	get() = isNotEmpty()
val CharSequence?.isNullOrEmpty: Boolean
	get() = isNullOrEmpty()
val CharSequence?.isNotNullOrEmpty: Boolean
	get() = !isNullOrEmpty()

val Collection<*>.isEmpty: Boolean
	get() = isEmpty()
val Collection<*>.isNotEmpty: Boolean
	get() = isNotEmpty()
val Collection<*>.isNullOrEmpty: Boolean
	get() = isNullOrEmpty()
val Collection<*>.isNotNullOrEmpty: Boolean
	get() = !isNullOrEmpty()
