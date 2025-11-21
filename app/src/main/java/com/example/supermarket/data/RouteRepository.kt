// =========================================================
// File: RouteRepository.kt
// 概要:
//   - 店舗内ルート計算に必要な「エリア」の情報を管理する。
//   - RouteScreen から利用され、エリアスライダーと店内マップの初期配置を提供する。
// 設計書ID: route（ロジック部）
// 画面名: 非UI（ルート計算ロジック）
// 役割:
//   - AreaNode（エリアID＋マップ上の中心座標）を定義。
//   - カテゴリ名（飲料・食品など）に応じた初期位置を返す。
// 備考:
//   - 本実装では「カテゴリ名」をそのままエリアIDとして扱っている。
//     将来「A〜F」などの専用エリアIDを導入する場合は、ここでマッピングを定義しなおす。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.data

/**
 * AreaNode
 * 店舗内の1つのエリア（ゾーン）を表す。
 *
 * @param id       エリアID（例: "飲料", "食品" など）
 * @param centerX  店内マップ上のX座標（0.0〜1.0 の範囲で正規化）
 * @param centerY  店内マップ上のY座標（0.0〜1.0 の範囲で正規化）
 */
data class AreaNode(
    val id: String,
    val centerX: Float,
    val centerY: Float
)

/**
 * RouteRepository
 * ルート計算用のエリア情報を提供するオブジェクト。
 */
object RouteRepository {

    // -----------------------------------------------------
    // 既定のエリア配置
    //   - key: カテゴリ名（= エリアIDとして使用）
//   - value: AreaNode（店内マップ上の相対座標）
// -----------------------------------------------------
    private val predefinedAreas: Map<String, AreaNode> = mapOf(
        "飲料" to AreaNode("飲料", centerX = 0.2f, centerY = 0.2f),
        "食品" to AreaNode("食品", centerX = 0.5f, centerY = 0.2f),
        "菓子" to AreaNode("菓子", centerX = 0.8f, centerY = 0.2f),
        "調味料" to AreaNode("調味料", centerX = 0.2f, centerY = 0.5f),
        "日用品" to AreaNode("日用品", centerX = 0.5f, centerY = 0.5f),
        "冷蔵" to AreaNode("冷蔵", centerX = 0.8f, centerY = 0.5f),
        "冷凍" to AreaNode("冷凍", centerX = 0.3f, centerY = 0.8f),
        "その他" to AreaNode("その他", centerX = 0.7f, centerY = 0.8f)
    )

    /**
     * buildInitialAreaOrder
     * カート内に登場するエリアIDの集合から、初期表示用のエリア順を作成する。
     *
     * @param areaIds  カート内で使用されているエリアIDの集合
     * @return         初期順で並べた AreaNode のリスト
     *
     * ルール:
     *   - 事前定義されているカテゴリ（飲料〜その他）は、predefinedAreasの並び順を尊重。
     *   - 未定義のIDが来た場合は、適当な位置（左上から右下への対角線上）に自動配置。
     */
    fun buildInitialAreaOrder(areaIds: Set<String>): List<AreaNode> {
        if (areaIds.isEmpty()) return emptyList()

        val result = mutableListOf<AreaNode>()

        // 1. 既知のエリアを事前定義の順番で追加
        val knownIdsInPredefinedOrder = predefinedAreas.keys.filter { areaIds.contains(it) }
        knownIdsInPredefinedOrder.forEach { id ->
            predefinedAreas[id]?.let { result.add(it) }
        }

        // 2. 未知のエリアIDに対して簡易的なレイアウトを割り当て
        val unknownIds = areaIds - predefinedAreas.keys
        if (unknownIds.isNotEmpty()) {
            val step = 1f / (unknownIds.size + 1)
            var index = 1
            unknownIds.sorted().forEach { id ->
                val pos = step * index
                result.add(
                    AreaNode(
                        id = id,
                        centerX = pos,
                        centerY = pos
                    )
                )
                index++
            }
        }

        return result
    }
// =========================================================
// 追加: 巡回セールスマン問題(TSP)ベースの簡易ルート計算
//   - 入力: カテゴリIDリスト（例: 飲料, 食品, 菓子...）
//   - 出力: 総移動距離が短くなる順番に並べ替えたリスト
//   - 注意: ここでは各カテゴリを円周上に仮配置して距離を定義している。
//           実店舗レイアウトに合わせたい場合は buildSimpleAreaPoints 内の座標を調整する。
// =========================================================

    data class SimpleAreaPoint(
        val id: String,
        val x: Double,
        val y: Double
    )

    /**
     * カテゴリIDから簡易的な座標を生成する。
     * 現段階では円形に均等配置しているだけの仮実装。
     */
    private fun buildSimpleAreaPoints(areaIds: List<String>): List<SimpleAreaPoint> {
        if (areaIds.isEmpty()) return emptyList()

        val distinctIds = areaIds.distinct()
        val radius = 100.0
        val step = 2.0 * Math.PI / distinctIds.size.coerceAtLeast(1)

        return distinctIds.mapIndexed { index, id ->
            val angle = step * index
            SimpleAreaPoint(
                id = id,
                x = radius * kotlin.math.cos(angle),
                y = radius * kotlin.math.sin(angle)
            )
        }
    }

    /**
     * 巡回セールスマン問題の全探索(最大8エリア想定)で、
     * 総移動距離が最も短くなるエリア順序を求める。
     */
    fun calcShortestAreaOrder(areaIds: List<String>): List<String> {
        val distinctIds = areaIds.distinct()
        if (distinctIds.size <= 1) return distinctIds

        val points = buildSimpleAreaPoints(distinctIds)
        if (points.size <= 1) return distinctIds

        val pointMap = points.associateBy { it.id }

        fun routeDistance(order: List<String>): Double {
            var sum = 0.0
            for (i in 0 until order.size - 1) {
                val a = pointMap[order[i]] ?: continue
                val b = pointMap[order[i + 1]] ?: continue
                val dx = a.x - b.x
                val dy = a.y - b.y
                sum += kotlin.math.sqrt(dx * dx + dy * dy)
            }
            return sum
        }

        var bestOrder = distinctIds
        var bestDistance = Double.MAX_VALUE

        fun permute(current: MutableList<String>, remaining: MutableList<String>) {
            if (remaining.isEmpty()) {
                val d = routeDistance(current)
                if (d < bestDistance) {
                    bestDistance = d
                    bestOrder = current.toList()
                }
                return
            }
            for (i in remaining.indices) {
                val id = remaining.removeAt(i)
                current.add(id)
                permute(current, remaining)
                current.removeAt(current.lastIndex)
                remaining.add(i, id)
            }
        }

        permute(mutableListOf(), distinctIds.toMutableList())
        return bestOrder
    }

}
