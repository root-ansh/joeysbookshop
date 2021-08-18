package work.curioustools.jb_mobile.utils

import androidx.annotation.Keep


//==================== <EXTENSIONS/> ===============================================================

//==================== <UTILS> =====================================================================

class StringValidators(var config: AuthConfig = AuthConfig()) {

    fun validateEmail(email: String?): AuthStringResponse {
        if (email.isNullOrBlank()) return AuthStringResponse.NULL_OR_BLANK
        if (email.length > config.emailMaxLength) return AuthStringResponse.EMAIL_MAX_LENGTH

        return if (email.matches(Regex(config.emailPattern))) {
            AuthStringResponse.VALID
        } else AuthStringResponse.OTHER_ERRORS("This email is not valid. Try again")

    }


    fun validateUserName(name: String?): AuthStringResponse {
        // allowed characters       : a-z | A-Z | 0-9 |basic special chars (#@$%^^*.. etc)
        // not allowed characters   : spaces,breaking characters( \ "  ' etc)
        // other notes              : no need to check for atleast 1 number or character. s="fullabcd" or"FULLfull" or "12345678" is also valid

        if (name.isNullOrBlank()) return AuthStringResponse.NULL_OR_BLANK
        if (name.length < config.userMin || name.length>config.userMax)
            return AuthStringResponse.OUT_OF_BOUNDS_ERROR(config.userMin, config.userMax)

        name.forEach {
            when (it) {
                in AuthConfig.CHARS_UPPER, in AuthConfig.CHARS_LOWER, in AuthConfig.CHARS_DIGIT, in AuthConfig.CHARS_SPECIAL_BASIC -> {

                }
                else -> return AuthStringResponse.CHARACTER_ERROR(it)
            }
        }


        return AuthStringResponse.VALID
    }
    fun validateEmailOrUSerName(emailOrUserName: String?): AuthStringResponse {
        if (emailOrUserName.isNullOrBlank()) return AuthStringResponse.NULL_OR_BLANK
        return AuthStringResponse.VALID
    }

    fun validatePasswordLogin(pwd: String?): AuthStringResponse {
        if (pwd.isNullOrBlank()) return AuthStringResponse.NULL_OR_BLANK
        return AuthStringResponse.VALID
    }

    fun validatePasswordSignup(pwd: String?): AuthStringResponse {
        // allowed characters       : a-z | A-Z | 0-9 |basic special chars (#@$%^^*.. etc)
        // not allowed characters   : spaces,breaking characters( \ "  ' etc)
        // other notes              : must have atleast 1 number , 1 special and 1 letter(any case)

        if (pwd.isNullOrBlank()) return AuthStringResponse.NULL_OR_BLANK
        if (pwd.length < config.pwdMin || pwd.length > config.pwdMax)
            return AuthStringResponse.OUT_OF_BOUNDS_ERROR(config.pwdMin, config.pwdMax)

        var has1Digit = false
        var has1Char = false
        var has1Special = false

        pwd.forEach {
            when (it) {
                in AuthConfig.CHARS_UPPER, in AuthConfig.CHARS_LOWER -> has1Char = true
                in AuthConfig.CHARS_DIGIT -> has1Digit = true
                in AuthConfig.CHARS_SPECIAL_BASIC -> has1Special = true
                else -> return AuthStringResponse.CHARACTER_ERROR(it)
            }
        }
        if (has1Char && has1Digit && has1Special) {
            return AuthStringResponse.OTHER_ERRORS("Must have at least 1 digit, 1 alphabet and 1  symbol")
        }

        return AuthStringResponse.VALID

    }


    //==================== <TESTCASES> ===========================================

    companion object{
        fun main() {
            val r = StringValidators()


            testEmail(r)
            println("=======================================================")
            testSimplePasswordValidator(r)
            println("=======================================================")
            testSimpleUserNameValidator(r)

        }

        fun testEmail(r: StringValidators) {

            println("prettyandsimple@example.com \t|\t ${r.validateEmail("prettyandsimple@example.com").msg}")
            println("very.common@example.com \t|\t ${r.validateEmail("very.common@example.com").msg}")
            println("disposable.style.email.with+symbol@example.com \t|\t ${r.validateEmail("disposable.style.email.with+symbol@example.com").msg}")
            println("other.email-with-dash@example.com \t|\t ${r.validateEmail("other.email-with-dash@example.com").msg}")

            //correct emails but not recognised by our pattern :(
            println("\" \"@example.org \t|\t ${r.validateEmail("\" \"@example.org").msg}")
            println("üñîçøðé@example.com \t|\t ${r.validateEmail("üñîçøðé@example.com").msg}")
            println("üñîçøðé@üñîçøðé.com \t|\t ${r.validateEmail("üñîçøðé@üñîçøðé.com").msg}")
            println("δοκιμή@παράδειγμα.δοκιμή \t|\t ${r.validateEmail("δοκιμή@παράδειγμα.δοκιμή").msg}")
            println("我買@屋企.香港 \t|\t ${r.validateEmail("我買@屋企.香港").msg}")
            println("甲斐@黒川.日本 \t|\t ${r.validateEmail("甲斐@黒川.日本").msg}")
            println("чебурашка@ящик-с-апельсинами.рф \t|\t ${r.validateEmail("чебурашка@ящик-с-апельсинами.рф").msg}")


        }

        fun testSimplePasswordValidator(r: StringValidators) {
            //pass cases |

            println(" ansh1234: ${r.validatePasswordSignup("ansh1234").msg}")
            println(" anshANSH1234: ${r.validatePasswordSignup("anshANSH1234").msg}")
            println(" ANSH1234: ${r.validatePasswordSignup("anshANSH1234").msg}")


            //fail cases |

            println(" anshsachdeva: ${r.validatePasswordSignup("anshsachdeva").msg}")                           // reason no numbers
            println(" ANSHSACHDEVA: ${r.validatePasswordSignup("ANSHSACHDEVA").msg}")                           // reason no numbers
            println(" 12345678: ${r.validatePasswordSignup("12345678").msg}")                                   // reason no letters
            println(" anshANSH: ${r.validatePasswordSignup("anshANSH").msg}")                                   // reason no numbers
            println(" ansh: ${r.validatePasswordSignup("ansh").msg}")                                           // reason size<8
            println(" ansh12345678ANSH12: ${r.validatePasswordSignup("ansh12345678ANSH12").msg}")               // reason size>16
            println(" `ansh sachdeva` : ${r.validatePasswordSignup("ansh sachdeva").msg}")                      // reason :space(special character)
            println("änsh1234 :  ${r.validatePasswordSignup("änsh1234").msg}")                                  // reason :ä    (special character)


        }

        fun testSimpleUserNameValidator(r: StringValidators) {
            //pass cases |

            println(" ansh1234: ${r.validateUserName("ansh1234").msg}")
            println(" anshANSH1234: ${r.validateUserName("anshANSH1234").msg}")
            println(" ANSH1234: ${r.validateUserName("anshANSH1234").msg}")
            println(" anshsachdeva: ${r.validateUserName("anshsachdeva").msg}")
            println(" ANSHSACHDEVA: ${r.validateUserName("ANSHSACHDEVA").msg}")
            println(" 12345678: ${r.validateUserName("12345678").msg}")
            println(" anshANSH: ${r.validateUserName("anshANSH").msg}")
            println(" `ans\$sachdeva` : ${r.validateUserName("ansh\$sachdeva").msg}")                      // reason :space(special character)


            //fail cases |
            println(" ansh: ${r.validateUserName("ansh").msg}")                                           // reason size<8
            println(" ansh12345678ANSH12: ${r.validateUserName("ansh12345678ANSH12").msg}")               // reason size>16
            println("änsh1234 :  ${r.validateUserName("änsh1234").msg}")                                  // reason :ä    (special character)

        }

    }

    //==================== </TESTCASES> ================================================

}

//==================== </UTILS> =====================================================================

//==================== <MISC> =====================================================================

@Keep
data class AuthConfig(
    val emailMaxLength: Int = EMAIL_MAX_LENGTH,
    val emailPattern: String = EMAIL_DEFAULT_VALIDATE_PATTERN,
    val pwdMin: Int = PWD_MIN_LENGTH,
    val pwdMax: Int = PWD_MAX_LENGTH,
    val userMin: Int = USERNAME_MIN_LENGTH,
    val userMax: Int = USERNAME_MAX_LENGTH,
) {
    companion object {
        const val EMAIL_DEFAULT_VALIDATE_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
        const val EMAIL_MAX_LENGTH = 100
        const val PWD_MIN_LENGTH = 8
        const val PWD_MAX_LENGTH = 16

        const val USERNAME_MIN_LENGTH = 8
        const val USERNAME_MAX_LENGTH = 16
        val CHARS_LOWER = 'a'..'z'
        val CHARS_UPPER = 'A'..'Z'
        val CHARS_DIGIT = '0'..'9'
        val CHARS_SPECIAL_BASIC = "~`!@#$%^&*()_-+={[}]|:;<,>.?/"// does not have \ " '

    }
}


@Keep
sealed class AuthStringResponse(open val msg: String) {
    //generic
    @Keep
    object VALID : AuthStringResponse("Success")

    @Keep
    object NULL_OR_BLANK : AuthStringResponse("Must not be empty")

    @Keep
    data class OUT_OF_BOUNDS_ERROR(val min: Int, val max: Int) :
        AuthStringResponse("Must have $min to $max characters")

    @Keep
    data class CHARACTER_ERROR(val c: Char) : AuthStringResponse(" '$c ' is not a valid character")

    @Keep
    data class OTHER_ERRORS(override val msg: String) : AuthStringResponse(msg)

    @Keep //email specific
    object EMAIL_MAX_LENGTH : AuthStringResponse("This email exceeds the maximum character limit")


}

//==================== </MISC> =====================================================================


