package com.aperfectpolygon.extentions

//region regex
val ZERO_WIDTH_SPACE: Regex by lazy { Regex("[\\p{javaWhitespace}\u00A0\u2007\u202F\u200c]+") }
val MATCH_USERNAME_SIGNUP: Regex by lazy { Regex("^\$|^(?=.{5,20}\$)(?![_.])(?!Candle_)(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$") }
val MATCH_USERNAME_PROFILE: Regex by lazy { Regex("^(?=.{5,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$") }
val MATCH_MOBILE_INTERNATIONAL: Regex by lazy { Regex("^\\d{3,15}\$") }
val MATCH_MOBILE: Regex by lazy {
	Regex(
		"^(?:\\+989|09|00989)([012349])[0-9]{8}\$|^\\+(999|998|997|996|995|994|993|992|991|990|979|978|977|976|975|974|973|972|971|970|969|968|967|966|965|964|963|962|961|960|899|898|897|896|895|894|893|892|891|890|889|888|887|886|885|884|883|882|881|880|879|878|877|876|875|874|873|872|871|870|859|858|857|856|855|854|853|852|851|850|839|838|837|836|835|834|833|832|831|830|809|808|807|806|805|804|803|802|801|800|699|698|697|696|695|694|693|692|691|690|689|688|687|686|685|684|683|682|681|680|679|678|677|676|675|674|673|672|671|670|599|598|597|596|595|594|593|592|591|590|509|508|507|506|505|504|503|502|501|500|429|428|427|426|425|424|423|422|421|420|389|388|387|386|385|384|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|296|295|294|293|292|291|290|289|288|287|286|285|284|283|282|281|280|269|268|267|266|265|264|263|262|261|260|259|258|257|256|255|254|253|252|251|250|249|248|247|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|219|218|217|216|215|214|213|212|211|210|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1)\\d{1,14}\$"
	)
}
val MATCH_MAIL: Regex by lazy { Regex("^$|^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$") }
val PASSWORD: Regex by lazy { Regex("^(?=.*[A-Za-z])(?=.*[0-9]).{8,}\$") }
val MATCH_NAME_LAST_NAME: Regex by lazy {
	Regex(
		"^$|^[\\u0621-\\u0628\\u062A-\\u063A\\u0641-\\u0642\\u0644-\\u0648\\u064E-\\u0651\\u0655\\u067E\\u0686\\u0698\\u0020\\u2000-\\u200F\\u2028-\\u202F\\u06A9\\u06AF\\u06BE\\u06CC\\u0629\\u0643\\u0649-\\u064B\\u064D\\u06D5A-Za-z]{3,30}\$"
	)
}
val String.isPasswordValid: Boolean
	get() = PASSWORD.matches(this)

val String.isEmailValid: Boolean
	get() = MATCH_MAIL.matches(this)

val String.isMobileValid: Boolean
	get() = MATCH_MOBILE.matches(this)

val String.isUserNameValid: Boolean
	get() {
		var value = this
		if (value.startsWith("+")) value = "00" + value.substring(1)
		return isNotEmpty() and (MATCH_USERNAME_SIGNUP.matches(value)
				or (MATCH_MOBILE.matches(value) or MATCH_MOBILE_INTERNATIONAL.matches(value)))
	}

private val String.isNameFamilyValid: Boolean
	get() = MATCH_NAME_LAST_NAME.matches(this)