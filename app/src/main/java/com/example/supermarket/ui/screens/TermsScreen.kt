// =========================================================
// File: TermsScreen.kt
// 設計書ID: terms
// 画面名: 利用規約画面
// 役割:
//   - 利用規約文言を表示する。
//   - 戻るボタンで前の画面に戻る。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("利用規約") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text =
                """
                第1条（適用）
                本規約は，本アプリ「スーパー導線ナビ」（以下「本アプリ」）の利用条件を定めるものです。
                ユーザーは，本アプリをインストールまたは利用した時点で，本規約に同意したものとみなします。

                第2条（本アプリの内容）
                1. 本アプリは，店舗内の商品配置情報や，買い物の参考となるルート情報などを表示することを目的としたアプリです。
                2. 本アプリに表示される商品情報（価格，在庫，配置場所など）は，あくまで参考情報であり，
                   実際の店舗の状況と異なる場合があります。

                第3条（注意事項）
                1. 本アプリの情報は，必ずしも最新かつ正確であることを保証するものではありません。
                2. ユーザーは，実際の購入にあたって，必ず店舗の表示価格や店員の案内を確認するものとします。

                第4条（禁止事項）
                ユーザーは，本アプリの利用にあたり，次の行為をしてはなりません。
                ・法令または公序良俗に違反する行為
                ・本アプリの運営を妨害する行為
                ・不正アクセスやリバースエンジニアリング等の行為
                ・他のユーザー，店舗，開発者等の権利・利益を侵害する行為

                第5条（免責事項）
                1. 開発者は，本アプリの利用によりユーザーまたは第三者に生じた損害について，
                   直接的・間接的を問わず，一切の責任を負いません。
                2. 本アプリに不具合や障害が発生した場合であっても，データの復旧や補償等の義務を負いません。

                第6条（サービスの変更・停止）
                開発者は，ユーザーへの事前通知なく，本アプリの内容を変更，追加，停止または終了することができます。

                第7条（個人情報の取扱い）
                本アプリ内で取得したユーザー情報がある場合は，学校の授業・卒業制作の範囲内でのみ利用し，
                それ以外の目的で利用・第三者提供を行うことはありません。

                第8条（準拠法・管轄）
                本規約の解釈には，日本法を準拠法とします。
                本アプリに関して紛争が生じた場合，日本国内の裁判所を第一審の専属的合意管轄裁判所とします。

                以上
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
