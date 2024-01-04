package me.bongle.utils.validate

private const val PASS_REGEX = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+\$).{8,24}$"
private const val NAME_REGEX = "^[\\u4e00-\\u9fa5_ya-zA-Z0-9]{3,16}$"
private const val EMAIL_REGEX = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"

private val allowedHosts = listOf(
    "gmail.com",
    "hotmail.com",
    "outlook.com",
    "yahoo.com",
    "icloud.com",
    "163.com",
    "126.com",
    "qq.com",
    "sina.com",
    "sohu.com",
    "aliyun.com",
    "yeah.net",
    "139.com",
    "foxmail.com",
    "vip.163.com",
    "vip.qq.com",
    "china.com",
    "21cn.com",
    "wps.cn",
    "recho.io"
)

fun validateName(name: String) = name matches Regex(NAME_REGEX)
fun validatePassword(password: String) = password matches Regex(PASS_REGEX)
fun validateEmail(email: String) =
    email.length <= 50 && email matches Regex(EMAIL_REGEX) && allowedHosts.any { email.endsWith(it) }