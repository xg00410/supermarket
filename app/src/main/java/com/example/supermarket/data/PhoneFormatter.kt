package com.example.supermarket.data

/**
 * 電話番号を xxx-xxxx-xxxx の形に自動整形するユーティリティ。
 * - 入力が 10桁 または 11桁 の数字であれば自動フォーマット。
 * - 数字以外の文字は全て除去。
 * - 正常に整形できない場合は元の文字列を返す。
 */
object PhoneFormatter {

    fun format(input: String?): String {
        if (input.isNullOrBlank()) return ""

        // 数字以外を全て除去
        val digits = input.filter { it.isDigit() }

        return when (digits.length) {
            10 -> {
                // 固定電話: 10桁 → 2-4-4 は地域によって違うため
                // 一般的な 3-3-4 にする（例：03 は例外だが今回は共通ルールで統一）
                "${digits.substring(0, 3)}-" +
                        "${digits.substring(3, 6)}-" +
                        digits.substring(6)
            }
            11 -> {
                // 携帯電話: 11桁 → 3-4-4（090-1234-5678）
                "${digits.substring(0, 3)}-" +
                        "${digits.substring(3, 7)}-" +
                        digits.substring(7)
            }
            else -> {
                // 整形不可能な場合（桁数不正）
                input
            }
        }
    }
}
