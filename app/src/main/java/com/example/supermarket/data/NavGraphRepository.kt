package com.example.supermarket.data

import com.example.supermarket.models.*
import com.example.supermarket.net.ApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.sqrt
import java.util.PriorityQueue

/**
 * NavGraphRepository
 * 店舗内ナビゲーション用データ・経路計算クラス
 *
 * 役割：
 *  - get_store_layout.php よりノード・エッジ・棚情報を取得する
 *  - キャッシュを利用して無駄な API 呼び出しを防止
 *  - 商品の accessPointId から最寄りノードを取得
 *  - ダイクストラ法による最短経路計算
 *  - 複数商品を効率的に回る簡易 TSP（最近隣法）
 *
 * 注意：
 *  - 既存の RouteRepository.kt や UI は一切変更しない
 *  - 完全に独立した追加モジュール
 */
class NavGraphRepository(
    private val apiService: ApiService
) {

    /** レイアウトキャッシュ（storeId ごと） */
    private val layoutCache = mutableMapOf<String, StoreLayoutResponse>()

    /** キャッシュ制御用ミューテックス */
    private val mutex = Mutex()

    /**
     * 店舗レイアウト取得（キャッシュ対応）
     */
    suspend fun getLayout(storeId: String): StoreLayoutResponse {
        return mutex.withLock {
            // キャッシュに存在すれば再取得不要
            layoutCache[storeId]?.let { return it }

            // API から取得
            val layout = apiService.getStoreLayout(storeId)

            // キャッシュ保存
            layoutCache[storeId] = layout

            layout
        }
    }

    /**
     * 入口ノード（isEntrance == true）を取得
     */
    suspend fun findEntranceNode(storeId: String): NavNode {
        val layout = getLayout(storeId)
        return layout.nodes.firstOrNull { it.isEntrance }
            ?: throw IllegalStateException("入口ノードが存在しません。")
    }

    /**
     * 商品の accessPointId → 最寄りノードを取得
     */
    suspend fun findNodeForProduct(storeId: String, item: CartItem): NavNode? {
        val layout = getLayout(storeId)

        val ap = layout.access_points.firstOrNull {
            it.accessPointId == item.accessPointId
        } ?: return null

        return layout.nodes.firstOrNull { it.nodeId == ap.nearestNodeId }
    }

    /**
     * ダイクストラ法による最短経路
     *
     * @return ノードのリスト（通過順）
     */
    suspend fun shortestPath(storeId: String, startId: String, endId: String): List<NavNode> {
        val layout = getLayout(storeId)

        // ノード辞書
        val nodeMap = layout.nodes.associateBy { it.nodeId }

        // 隣接リスト構築
        val graph = mutableMapOf<String, MutableList<Pair<String, Float>>>()
        for (edge in layout.edges) {
            val dist = edge.distance ?: calcDistance(
                nodeMap[edge.fromNodeId]!!,
                nodeMap[edge.toNodeId]!!
            )
            graph.getOrPut(edge.fromNodeId) { mutableListOf() }
                .add(edge.toNodeId to dist)
            graph.getOrPut(edge.toNodeId) { mutableListOf() }
                .add(edge.fromNodeId to dist) // 無向想定
        }

        // コスト表
        val dist = mutableMapOf<String, Float>()
        // 前ノード
        val prev = mutableMapOf<String, String?>()

        // 初期化
        for (node in nodeMap.keys) {
            dist[node] = Float.POSITIVE_INFINITY
            prev[node] = null
        }
        dist[startId] = 0f

        // 優先度付きキュー
        val pq = PriorityQueue(compareBy<Pair<String, Float>> { it.second })
        pq.add(startId to 0f)

        while (pq.isNotEmpty()) {
            val (currentId, currentDist) = pq.poll()

            if (currentDist > dist[currentId]!!) continue
            if (currentId == endId) break

            val neighbors = graph[currentId] ?: continue

            for ((nextId, weight) in neighbors) {
                val newDist = currentDist + weight
                if (newDist < dist[nextId]!!) {
                    dist[nextId] = newDist
                    prev[nextId] = currentId
                    pq.add(nextId to newDist)
                }
            }
        }

        // 経路復元
        val path = mutableListOf<String>()
        var node: String? = endId
        while (node != null) {
            path.add(node)
            node = prev[node]
        }
        path.reverse()

        return path.mapNotNull { nodeMap[it] }
    }

    /**
     * 2 点間の距離（x, y は 0〜1 の正規化座標）
     */
    private fun calcDistance(a: NavNode, b: NavNode): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * 複数商品を最近隣法で巡回し、総経路ノードを生成
     */
    suspend fun buildRouteForProducts(
        storeId: String,
        startNodeId: String,
        items: List<CartItem>
    ): List<NavNode> {

        val remaining = items.toMutableList()
        val route = mutableListOf<NavNode>()
        var current = startNodeId

        while (remaining.isNotEmpty()) {

            // 最短距離の商品を探索
            val bestItem = remaining.minByOrNull { item ->
                val node = findNodeForProduct(storeId, item)
                if (node == null) Float.POSITIVE_INFINITY
                else {
                    val path = shortestPath(storeId, current, node.nodeId)
                    pathDistance(path)
                }
            } ?: break

            // 商品の最寄りノード
            val targetNode = findNodeForProduct(storeId, bestItem)
            if (targetNode == null) {
                remaining.remove(bestItem)
                continue
            }

            // 経路追加
            val segment = shortestPath(storeId, current, targetNode.nodeId)
            if (route.isNotEmpty() && segment.isNotEmpty() &&
                route.last().nodeId == segment.first().nodeId) {
                route.addAll(segment.drop(1))
            } else {
                route.addAll(segment)
            }

            current = targetNode.nodeId
            remaining.remove(bestItem)
        }

        return route
    }

    /**
     * 経路（ノード列）の総距離
     */
    private fun pathDistance(path: List<NavNode>): Float {
        var sum = 0f
        for (i in 0 until path.size - 1) {
            sum += calcDistance(path[i], path[i + 1])
        }
        return sum
    }
}
