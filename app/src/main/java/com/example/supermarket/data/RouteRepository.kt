package com.example.supermarket.data

import kotlin.math.pow
import kotlin.math.sqrt

// 模拟每个商品在商店平面图中的位置
data class ProductPosition(
    val productId: String,
    val name: String,
    val x: Double,
    val y: Double
)

object RouteRepository {
    // 假设商店是 10x10 的格子区域
    private val productPositions = listOf(
        ProductPosition("p001", "お〜いお茶 500ml", 1.0, 2.0),
        ProductPosition("p002", "カップラーメン 醤油", 3.0, 5.0),
        ProductPosition("p010", "コカ・コーラ 1.5L", 8.0, 1.0),
        ProductPosition("p020", "チョコスナック", 9.0, 7.0),
        ProductPosition("p030", "ポテトチップス", 4.0, 8.0)
    )

    // 获取位置
    fun getPositionById(productId: String): ProductPosition? {
        return productPositions.find { it.productId == productId }
    }

    // 计算两点距离
    private fun distance(a: ProductPosition, b: ProductPosition): Double {
        return sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2))
    }

    // 简易TSP算法：找出一条最短路径
    fun getShortestRoute(productIds: List<String>): List<ProductPosition> {
        val points = productIds.mapNotNull { getPositionById(it) }.toMutableList()
        if (points.isEmpty()) return emptyList()

        val route = mutableListOf<ProductPosition>()
        var current = points.first()
        route.add(current)
        points.remove(current)

        while (points.isNotEmpty()) {
            val next = points.minByOrNull { distance(current, it) }!!
            route.add(next)
            points.remove(next)
            current = next
        }

        return route
    }
}
