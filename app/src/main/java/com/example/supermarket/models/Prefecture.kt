// =========================================================
// File: Prefecture.kt
// 画面名: 都道府県マスターモデル
// 役割:
//   - 日本の 47 都道府県を表すマスターデータ。
//   - store_2（都道府県選択画面）、store_result（検索結果）で利用。
// =========================================================

package com.example.supermarket.models

/**
 * 都道府県マスターデータ。
 *
 * @param prefectureId  都道府県ID（1〜47）
 * @param regionId      所属する地域ID（1〜8）
 * @param prefectureName 都道府県名（例：東京都）
 */
data class Prefecture(
    val prefectureId: Int,
    val regionId: Int,
    val prefectureName: String
)
