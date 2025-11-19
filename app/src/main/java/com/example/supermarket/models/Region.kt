// =========================================================
// File: Region.kt
// 画面名: 地域マスターモデル
// 役割:
//   - 日本を 8 地域に分けたマスターデータ。
//   - 将来的に「地域選択画面」で利用予定。
// =========================================================

package com.example.supermarket.models

/**
 * 地域（北海道／東北／関東…）を表すシンプルなモデル。
 *
 * @param regionId  地域ID（1〜8）
 * @param regionName 地域名（例：関東）
 */
data class Region(
    val regionId: Int,
    val regionName: String
)
