// =========================================================
// File: RouteRepository.kt
// 最短ルート用エリアノード管理（最終版）
// =========================================================

package com.example.supermarket.data

data class AreaNode(
    val id: String          // エリアID（飲料・食品など）
)

object RouteRepository {

    fun buildInitialAreaOrder(areaIds: Set<String>): List<AreaNode> {
        return areaIds.map { AreaNode(it) }
    }
}
