package com.example.supermarket.data

import com.example.supermarket.models.*
import com.example.supermarket.net.ApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.sqrt
import android.util.Log
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
 *  - 複数商品を効率的に回るルート計算
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
     * 商品の accessPointId → 経路計算に使用するノードを取得
     *
     * ・基本方針：
     *   1) CartItem.accessPointId が shelf_access_points.access_point_id と一致する場合
     *      -> そのアクセスポイントを使う
     *   2) そうでない場合（例：DB に棚ID "A" / "E1" などが入っている場合）
     *      -> CartItem.shelfId を使って、その棚に属するアクセスポイント一覧から
     *         代表となる1点を選ぶ（M側優先）
     *   3) 最終的に選んだアクセスポイントの nearestNodeId を最優先し、
     *      ノードが見つからなければ座標的に一番近いノードを使う
     */
    suspend fun findNodeForProduct(storeId: String, item: CartItem): NavNode? {
        val layout = getLayout(storeId)

        val apIdFromItem = item.accessPointId
        val shelfIdFromItem = item.shelfId

        // ----------------------------------------------------
        // 1) まず accessPointId を「そのまま」キーとして探す
        //    （DB が "AP_A_M" などの形式に統一されている場合）
        // ----------------------------------------------------
        var ap = layout.access_points.firstOrNull { it.accessPointId == apIdFromItem }

        // ----------------------------------------------------
        // 2) 見つからなければ、棚IDベースで代表アクセスポイントを選ぶ
        //    例：store_products.access_point_id に "A" / "E1" が入っているケース
        // ----------------------------------------------------
        if (ap == null && shelfIdFromItem != null) {
            // この棚に属する AP 一覧
            val candidates = layout.access_points.filter { it.shelfId == shelfIdFromItem }

            if (candidates.isNotEmpty()) {
                ap = when {
                    // 「_M」で終わる中心側(AP_A_M 等) があればそれを優先
                    candidates.any { it.accessPointId?.endsWith("_M") == true } ->
                        candidates.first { it.accessPointId?.endsWith("_M") == true }

                    // それ以外は X 座標の中央値付近を代表として選ぶ
                    else -> {
                        val sorted = candidates.sortedBy { it.x }
                        sorted[sorted.size / 2]
                    }
                }
            }
        }

        // それでも見つからなければ、この商品はルート計算対象外
        val useAp = ap ?: return null

        // ----------------------------------------------------
        // 3) nearestNodeId を最優先で使用
        // ----------------------------------------------------
        val byId = layout.nodes.firstOrNull { it.nodeId == useAp.nearestNodeId }

        // 念のため、AP 座標に最も近いノードも計算しておく
        val nearestByPosition = layout.nodes.minByOrNull { node ->
            val dx = node.x - useAp.x
            val dy = node.y - useAp.y
            dx * dx + dy * dy      // 距離の二乗（sqrt は不要）
        }

        // 優先順位：nearestNodeId → 座標で最も近いノード → null
        return byId ?: nearestByPosition
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

    // =========================================================
// 内部共通ヘルパー
//   複数商品のノードを「縦レイヤー → X座標方向スイープ」で走査しながら、
//   最短経路を順に連結する。
//   ・各レイヤー内の走査方向は、現在位置の X 座標に応じて
//     左→右 または 右→左 を自動選択する。
//   戻り値:
//     Pair(生成した経路ノード一覧, 最後に到達したノードID)
// =========================================================
    private suspend fun buildLayeredSweepRoute(
        storeId: String,
        startNodeId: String,
        items: List<CartItem>
    ): Pair<List<NavNode>, String> {

        val layout = getLayout(storeId)
        val nodeMap = layout.nodes.associateBy { it.nodeId }

        // 結果経路と現在位置
        val route = mutableListOf<NavNode>()
        var currentNodeId = startNodeId

        // 開始ノードを経路の先頭に入れておく
        nodeMap[currentNodeId]?.let { route.add(it) }

        if (items.isEmpty()) {
            // 対象商品が無い場合は開始ノードのみ返す
            return route to currentNodeId
        }

        // 1) 各商品に対して「対応ノード」と「縦レイヤー」を計算しておく
        data class Target(
            val item: CartItem,
            val node: NavNode,
            val layer: Int
        )

        val targets = mutableListOf<Target>()
        for (item in items) {
            val node = findNodeForProduct(storeId, item) ?: continue
            val layer = getVerticalLayer(node)
            targets.add(Target(item, node, layer))
        }

        if (targets.isEmpty()) {
            // ノードが一つも取得できなかった場合も開始ノードのみ
            return route to currentNodeId
        }

        // 2) レイヤーごとにグループ化（この段階では順序は固定しない）
        val groupedByLayer: Map<Int, List<Target>> =
            targets.groupBy { it.layer }

        // 3) 上段(0) → 中段(1) → 下段(2) の順で処理
        for (layer in listOf(0, 1, 2)) {
            val inLayer = groupedByLayer[layer] ?: continue
            if (inLayer.isEmpty()) continue

            // このレイヤー内の X 座標の範囲と中央
            val minX = inLayer.minOf { it.node.x }
            val maxX = inLayer.maxOf { it.node.x }
            val midX = (minX + maxX) / 2f

            // 現在位置ノードの X 座標（取得できなければ中央扱い）
            val currentX = layout.nodes.firstOrNull { it.nodeId == currentNodeId }?.x ?: midX

            // 現在位置が左側なら左→右、右側なら右→左に掃き切る
            val ordered: List<Target> = if (currentX <= midX) {
                inLayer.sortedBy { it.node.x }
            } else {
                inLayer.sortedByDescending { it.node.x }
            }

            for (t in ordered) {
                // 現在位置からターゲットノードまでの最短経路
                val path = shortestPath(storeId, currentNodeId, t.node.nodeId)
                if (path.size < 2) {
                    // 経路が取れない場合はスキップ
                    continue
                }

                // 先頭ノードは現在位置と重複するので除外して連結
                route.addAll(path.drop(1))
                currentNodeId = t.node.nodeId
            }
        }

        return route to currentNodeId
    }


    /**
     * 最短ルート計算（エリア順序なし版・縦レイヤー＋X座標スイープ版）
     *
     * - 入口ノード(startNodeId) からスタートし、
     *   カート内の全商品を 1 回ずつ訪問するルートを構築する。
     * - ノードの y 座標から「上段／中段／下段」のレイヤーを求め、
     *   レイヤー順（上 → 中 → 下）の順番で処理する。
     * - 各レイヤー内部では X 座標昇順に「一方向スイープ」することで、
     *   同じ通路を何度も往復しない、わかりやすいルートになるようにする。
     */
    suspend fun buildRouteForProducts(
        storeId: String,
        startNodeId: String,
        items: List<CartItem>
    ): List<NavNode> {

        val (route, _) = buildLayeredSweepRoute(
            storeId = storeId,
            startNodeId = startNodeId,
            items = items
        )
        return route
    }

    /**
     * 複数商品を「エリア順序」に従って巡回し、総経路ノードを生成する
     *
     * エリア順序:
     *   - RouteScreen 下部のスライダーで決めたカテゴリ順
     *   - まず areaOrder の 1 番目のカテゴリの商品だけを
     *     上段→中段→下段、かつ X 座標昇順でスイープしながら巡回
     *   - 次に 2 番目のカテゴリ…という順番で巡回
     *   - areaOrder に含まれないカテゴリの商品は、最後に同じ方式でまとめて巡回
     */
    suspend fun buildRouteForProductsWithAreaOrder(
        storeId: String,
        startNodeId: String,
        items: List<CartItem>,
        areaOrder: List<String>
    ): List<NavNode> {

        // まだ訪問していない商品
        val remaining = items.toMutableList()
        val route = mutableListOf<NavNode>()
        var current = startNodeId
        var isFirstSegment = true

        // 1) ユーザーが指定したエリア順序に従って、カテゴリ単位で巡回
        for (area in areaOrder) {
            // このエリア（カテゴリ）に属する、まだ訪問していない商品だけを取り出す
            val inArea = remaining.filter { it.category == area }
            if (inArea.isEmpty()) continue

            // 共通ヘルパーで「上→中→下」「X座標昇順」の経路を生成
            val (segment, lastNodeId) = buildLayeredSweepRoute(
                storeId = storeId,
                startNodeId = current,
                items = inArea
            )

            if (segment.isNotEmpty()) {
                if (isFirstSegment) {
                    // 最初のセグメントはそのまま連結
                    route.addAll(segment)
                    isFirstSegment = false
                } else {
                    // それ以降は先頭ノードが直前の終点と重複するので除外して連結
                    route.addAll(segment.drop(1))
                }
                current = lastNodeId
            }

            // 今回訪問し終えた商品を remaining から除外
            remaining.removeAll(inArea.toSet())
        }

        // 2) areaOrder に含まれなかった残りの商品をまとめて巡回
        if (remaining.isNotEmpty()) {
            val (segment, lastNodeId) = buildLayeredSweepRoute(
                storeId = storeId,
                startNodeId = current,
                items = remaining
            )

            if (segment.isNotEmpty()) {
                if (route.isEmpty()) {
                    route.addAll(segment)
                } else {
                    route.addAll(segment.drop(1))
                }
                current = lastNodeId
            }
        }

        return route
    }

    /**
     * デバッグ用：
     *  - nav_edges の各エッジが、どの棚矩形と交差しているかをログ出力する
     *
     * 使用例：
     *  - RouteScreen の LaunchedEffect 内で navRepo.debugLogEdgesCrossingShelves("TEST01")
     *    を 1 回だけ呼び出して、Logcat で問題のエッジIDを確認する。
     */
    suspend fun debugLogEdgesCrossingShelves(storeId: String) {
        val layout = getLayout(storeId)
        val nodeMap = layout.nodes.associateBy { it.nodeId }

        for (edge in layout.edges) {
            val from = nodeMap[edge.fromNodeId] ?: continue
            val to = nodeMap[edge.toNodeId] ?: continue

            for (shelf in layout.shelves) {
                if (segmentIntersectsShelf(from.x, from.y, to.x, to.y, shelf)) {
                    Log.d(
                        "NavDebug",
                        "edgeId=${edge.edgeId}, from=${edge.fromNodeId}, to=${edge.toNodeId} crosses shelfId=${shelf.shelfId}"
                    )
                    break
                }
            }
        }
    }

    /**
     * 線分（x1,y1)-(x2,y2) が棚矩形と交差しているかどうか
     *
     * 棚の x,y は中心座標、width/height は全幅・全高とみなし、
     * そこから矩形の四隅を計算する。
     */
    private fun segmentIntersectsShelf(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        shelf: Shelf
    ): Boolean {
        val left = shelf.x - shelf.width / 2f
        val right = shelf.x + shelf.width / 2f
        val top = shelf.y - shelf.height / 2f
        val bottom = shelf.y + shelf.height / 2f

        // 1) バウンディングボックスで早期除外
        if (maxOf(x1, x2) < left || minOf(x1, x2) > right ||
            maxOf(y1, y2) < top || minOf(y1, y2) > bottom
        ) {
            return false
        }

        // 2) 両端点が矩形内にある場合は交差とみなす
        val inside1 = x1 in left..right && y1 in top..bottom
        val inside2 = x2 in left..right && y2 in top..bottom
        if (inside1 && inside2) return true

        // 3) 矩形の4辺との線分交差判定
        val rectEdges = listOf(
            floatArrayOf(left, top, right, top),       // 上辺
            floatArrayOf(right, top, right, bottom),   // 右辺
            floatArrayOf(right, bottom, left, bottom), // 下辺
            floatArrayOf(left, bottom, left, top)      // 左辺
        )

        for (e in rectEdges) {
            if (segmentsIntersect(x1, y1, x2, y2, e[0], e[1], e[2], e[3])) {
                return true
            }
        }

        return false
    }

    /**
     * 2 本の線分 (ax,ay)-(bx,by), (cx,cy)-(dx,dy) が交差しているかを判定
     */
    private fun segmentsIntersect(
        ax: Float, ay: Float,
        bx: Float, by: Float,
        cx: Float, cy: Float,
        dx: Float, dy: Float
    ): Boolean {
        fun ccw(px: Float, py: Float, qx: Float, qy: Float, rx: Float, ry: Float): Float {
            return (qx - px) * (ry - py) - (qy - py) * (rx - px)
        }

        val c1 = ccw(ax, ay, bx, by, cx, cy)
        val c2 = ccw(ax, ay, bx, by, dx, dy)
        val c3 = ccw(cx, cy, dx, dy, ax, ay)
        val c4 = ccw(cx, cy, dx, dy, bx, by)

        if (c1 == 0f && c2 == 0f && c3 == 0f && c4 == 0f) {
            // 同一直線上：バウンディングボックスで重なりを確認
            val minAxBx = minOf(ax, bx)
            val maxAxBx = maxOf(ax, bx)
            val minCxDx = minOf(cx, dx)
            val maxCxDx = maxOf(cx, dx)

            val minAyBy = minOf(ay, by)
            val maxAyBy = maxOf(ay, by)
            val minCyDy = minOf(cy, dy)
            val maxCyDy = maxOf(cy, dy)

            return !(maxAxBx < minCxDx || maxCxDx < minAxBx ||
                    maxAyBy < minCyDy || maxCyDy < minAyBy)
        }

        return (c1 * c2 <= 0f) && (c3 * c4 <= 0f)
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

    /**
     * 縦方向レイヤー判定用
     *
     * ・ノードの y 座標から、大まかな「上段／中段／下段」を決める。
     *   - 0.35 未満   → 上段 (0)
     *   - 0.35〜0.62 → 中段 (1)
     *   - 0.62 以上  → 下段 (2)
     *
     *  ※ TEST01 の座標設計:
     *     上通路   y ≒ 0.25
     *     中通路   y ≒ 0.50
     *     下通路   y ≒ 0.75
     *   に合わせてしきい値を設定している。
     */
    private fun getVerticalLayer(node: NavNode): Int {
        return when {
            node.y < 0.35f -> 0   // 上段
            node.y < 0.62f -> 1   // 中段
            else -> 2             // 下段
        }
    }
}
