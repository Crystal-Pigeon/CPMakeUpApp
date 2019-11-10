package com.crystalpigeon.makeupapp

import java.util.regex.Pattern

object Consts {
    val PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")!!
}