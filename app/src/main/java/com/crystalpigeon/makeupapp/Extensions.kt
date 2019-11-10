package com.crystalpigeon.makeupapp

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.crystalpigeon.makeupapp.view.ILoader

fun Context.showLoader() = (this as? ILoader?)?.show()

fun Context.hideLoader() = (this as? ILoader?)?.hide()

fun Context.checkRequiredFields(vararg editTexts: EditText): Boolean {
    var isValid = true
    val drawable = getDrawable(R.mipmap.ic_error)
    drawable?.setBounds(0, 0, drawable.minimumWidth-25, drawable.minimumHeight-25)
    for (item in editTexts) {
        val empty = TextUtils.isEmpty(item.text.toString())
        if (empty) {
            item.setError(getString(R.string.required_field), drawable)
            isValid = false
        } else {
            item.setError(null, null)
        }
    }
    return isValid
}

fun noRequiredFields(vararg editTexts: EditText) {
    for (item in editTexts) {
        item.setError(null, null)
    }
}

fun Context.isEmailValid(email: EditText): Boolean {
    val valid = Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()
    val drawable = getDrawable(R.mipmap.ic_error)
    drawable?.setBounds(0, 0, drawable.minimumWidth-25, drawable.minimumHeight-25)
    if (!valid) {
        email.setError(getString(R.string.invalid_mail), drawable)
        return false
    } else {
        email.setError(null, null)
    }

    return true
}

fun Context.isPasswordValid(pass: EditText): Boolean {
    val text = pass.text.toString()
    val drawable = getDrawable(R.mipmap.ic_error)
    drawable?.setBounds(0, 0, drawable.minimumWidth-25, drawable.minimumHeight-25)
    val valid = Consts.PASSWORD_PATTERN.matcher(text).matches()

    if (!valid) {
        pass.setError(getString(R.string.must_contain_at_least_one_lower_case_and_number), drawable)
    }

    return valid
}

fun Context.isConfirmPassValid(pass: EditText, confirmPass: EditText): Boolean {
    val valid = pass.text.toString() == confirmPass.text.toString()
    val drawable = getDrawable(R.mipmap.ic_error)
    drawable?.setBounds(0, 0, drawable.minimumWidth-25, drawable.minimumHeight-25)
    if (!valid) {
        confirmPass.setError(getString(R.string.invalid_confirm_password), drawable)
    }
    return valid
}

fun Activity.hideKeyboard(){
    val view = this.currentFocus
    view?.let { v ->
        v.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}