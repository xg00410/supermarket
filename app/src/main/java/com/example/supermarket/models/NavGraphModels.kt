package com.example.supermarket.models

/**
 * 店舗内ナビゲーション用モデル
 */

data class NavNode(
    val nodeId: String,
    val x: Float,          // 0.0〜1.0
    val y: Float,          // 0.0〜1.0
    val isEntrance: Boolean
)

data class NavEdge(
    val edgeId: Long,
    val fromNodeId: String,
    val toNodeId: String,
    val distance: Float?   // NULL の場合は座標計算で距離算出
)

data class Shelf(
    val shelfId: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isAgainstWall: Boolean
)

data class ShelfAccessPoint(
    val accessPointId: String,
    val shelfId: String,
    val x: Float,
    val y: Float,
    val nearestNodeId: String,
    val side: String       // "TOP" / "BOTTOM" / "LEFT" / "RIGHT"
)

/**
 * get_store_layout.php のレスポンス用
 */
data class StoreLayoutResponse(
    val status: String,
    val nodes: List<NavNode>,
    val edges: List<NavEdge>,
    val shelves: List<Shelf>,
    val access_points: List<ShelfAccessPoint>
)
