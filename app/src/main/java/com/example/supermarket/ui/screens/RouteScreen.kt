// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - カート内商品のカテゴリを元にエリア順序（自動ルート）を算出。
//   - ユーザーがエリア順序を < / > ボタンで手動変更できる。
//   - 下部に対象商品の簡易一覧（商品名 + 数量）を表示。
//   - 「終了」ボタンでチェック済み商品の注文を DB に登録し、
//     ローカル履歴追加＋カートから削除する。
//ダイクストラ法（Dijkstra 法）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.supermarket.R
import com.example.supermarket.data.RouteRepository
import com.example.supermarket.data.UserSession
import com.example.supermarket.models.InsertOrderBody
import com.example.supermarket.models.OrderItem
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // -----------------------------------------------------
// ★ ルートノードの状態（Canvas はこれを描画するだけ）
// -----------------------------------------------------
    var routeNodesState by remember { mutableStateOf<List<com.example.supermarket.models.NavNode>>(emptyList()) }

    // ---------------- 対象店舗のカート商品 ----------------
    val cartItemsInStore by remember {
        derivedStateOf {
            cartViewModel.cartItems.filter { it.storeId == storeId }
        }
    }

    // ---------------- 店舗名（カート内から取得） ----------------
    val storeName by remember {
        derivedStateOf {
            cartItemsInStore.firstOrNull()?.storeName ?: ""
        }
    }

// -----------------------------------------------------
// ★ 店舗内ナビゲーション用（NavGraphRepository）
// -----------------------------------------------------
    val apiService = remember { ApiClient.retrofit.create(ApiService::class.java) }
    val navRepo = remember { com.example.supermarket.data.NavGraphRepository(apiService) }

// 店舗レイアウト（nodes / edges / shelves / access_points）
    val storeLayout by produceState(
        initialValue = null as com.example.supermarket.models.StoreLayoutResponse?
    ) {
        value = try {
            navRepo.getLayout(storeId)
        } catch (e: Exception) {
            null
        }
    }
    // ---------------- デバッグ：カート内商品とアクセスポイント分布の確認 ----------------
    LaunchedEffect(storeLayout, cartItemsInStore) {
        val tag = "RouteDebug"

        // カート全体の概要
        val totalItems = cartItemsInStore.size
        val itemsWithAp = cartItemsInStore.count { it.accessPointId != null }

        val apIdsFromCart = cartItemsInStore
            .mapNotNull { it.accessPointId }
            .toSet()

        Log.d(tag, "---- Route Debug Start ----")
        Log.d(tag, "storeId=$storeId, cartItemsInStore=$totalItems, itemsWithAp=$itemsWithAp")
        Log.d(tag, "uniqueApIdsFromCart(${apIdsFromCart.size})=$apIdsFromCart")

        // カート内：棚ごとの AP 利用状況
        val groupedByShelf = cartItemsInStore.groupBy { it.shelfId ?: "(null)" }
        groupedByShelf.forEach { (shelfId, items) ->
            val apSet = items.mapNotNull { it.accessPointId }.toSet()
            Log.d(
                tag,
                "Cart shelf=$shelfId, items=${items.size}, apIds=${apSet.ifEmpty { setOf("(none)") }}"
            )
        }

        // レイアウト側：棚ごとの AP 定義状況
        val layout = storeLayout
        if (layout != null) {
            val apByShelf = layout.access_points.groupBy { it.shelfId }
            apByShelf.forEach { (shelfId, aps) ->
                val ids = aps.map { it.accessPointId }
                Log.d(
                    tag,
                    "Layout shelf=$shelfId, definedAPs=$ids"
                )
            }
        }

        Log.d(tag, "---- Route Debug End ----")
    }


    // ---------------- 商品ごとのチェック状態（購入するかどうか） ----------------
    val checkedMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }
    // カート内容変化時に初期値をリセット（全て false）
    LaunchedEffect(cartItemsInStore) {
        checkedMap.clear()
        cartItemsInStore.forEach { item ->
            checkedMap[item.productId] = false
        }
    }

    // ---------------- エリア順序（実際の巡回順序に基づく） ----------------
    // 実際のルート巡回順序に基づいたエリアと商品の順序
    var sortedAreaOrder by remember { mutableStateOf<List<String>>(emptyList()) }
    var sortedCartItems by remember { mutableStateOf<List<com.example.supermarket.models.CartItem>>(emptyList()) }

    // -----------------------------------------------------
    // ★ カテゴリ → エリア対応（表示用・固定）
    //   - ユーザー要望により、エリア滑块は DB 参照せず固定文字列を表示する
    //   - 正確なエリア表示は、商品カード右上で行う
    // -----------------------------------------------------
    val categoryAreaLabelMap: Map<String, String> = mapOf(
        "飲料" to "飲料：I",
        "食品" to "食品：A",
        "菓子" to "菓子：B",
        "調味料" to "調味料：C",
        "日用品" to "日用品：D",
        "冷蔵" to "冷蔵：E1,E2",
        "冷凍" to "冷凍：F1,F2",
        "その他" to "その他：G1,G2,H1,H2"
    )

    // -----------------------------------------------------
    // ★ 商品のエリアコード（右上表示用）
    //   - shelfId があればそれを優先（例：A / B / E1）
    //   - 無い場合は accessPointId (例：AP_I) から推定
    // -----------------------------------------------------
    fun resolveAreaCode(item: com.example.supermarket.models.CartItem): String {
        val shelf = item.shelfId?.trim()
        if (!shelf.isNullOrEmpty()) return shelf

        val ap = item.accessPointId?.trim()
        if (!ap.isNullOrEmpty()) {
            return ap.removePrefix("AP_")
        }
        return "-"
    }

    // ---------------- 注文登録用 ----------------
    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    var isSending by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("最短ルート (${storeName})")
                },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        bottomBar = {
            // 画面下部中央の「終了」ボタン
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val userId = UserSession.userId
                        if (userId == null) {
                            dialogMessage = "ログイン情報が見つかりません。先にログインしてください。"
                            return@Button
                        }

                        val checkedItems =
                            cartItemsInStore.filter { checkedMap[it.productId] == true }
                        if (checkedItems.isEmpty()) {
                            navController.popBackStack()
                            return@Button
                        }

                        isSending = true

                        scope.launch {
                            try {
                                // DB へ注文登録
                                val orderItems = checkedItems.map {
                                    OrderItem(
                                        product_id = it.productId,
                                        quantity = it.quantity,
                                        price = it.price
                                    )
                                }

                                val body = InsertOrderBody(
                                    user_id = userId,
                                    store_code = storeId, // storeId をそのまま店舗コードとして送信
                                    items = orderItems
                                )

                                val res = api.insertOrder(body)
                                if (res.status == "ok") {
                                    // ローカル履歴に追加
                                    cartViewModel.addHistoryEntry(
                                        storeId = storeId,
                                        storeName = storeName,
                                        items = checkedItems,
                                        orderedAt = LocalDateTime.now()
                                    )

                                    // カートから削除
                                    val checkedIds =
                                        checkedItems.map { it.productId }.toSet()
                                    cartViewModel.removeCheckedItems(checkedIds)

                                    isSending = false
                                    // ★ 成功時：ダイアログは出さず、そのまま前の画面へ戻る
                                    navController.popBackStack()
                                } else {
                                    isSending = false
                                    dialogMessage =
                                        res.message ?: "注文登録に失敗しました。"
                                }
                            } catch (e: Exception) {
                                isSending = false
                                dialogMessage = "通信エラーが発生しました。"
                            }
                        }
                    },
                    enabled = !isSending,
                    modifier = Modifier
                        .widthIn(min = 160.dp)
                        .height(46.dp) // 少し小さめの高さ
                ) {
                    Text(if (isSending) "送信中..." else "終了")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---------------- 店内マップ（大きめに表示） ----------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f), // 全体のうち大部分を地図
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEAEAEA)),
                    contentAlignment = Alignment.Center
                ) {
                    val floorMapRes = R.drawable.store_floor_map
                    AsyncImage(
                        model = floorMapRes,
                        contentDescription = "store map",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
// ======================================================
// ★ 店舗内ルート描画（Canvas）
// ======================================================
                    if (storeLayout != null) {
                        // ※ エリア順序は固定表示のみ（変更イベントが無いので再計算も不要）
                        LaunchedEffect(storeLayout, cartItemsInStore) {

                            if (storeLayout == null) {
                                routeNodesState = emptyList()
                                sortedAreaOrder = emptyList()
                                sortedCartItems = emptyList()
                                return@LaunchedEffect
                            }

                            val entrance = storeLayout!!.nodes.firstOrNull { it.isEntrance }
                            if (entrance == null) {
                                routeNodesState = emptyList()
                                sortedAreaOrder = emptyList()
                                sortedCartItems = emptyList()
                                return@LaunchedEffect
                            }


// ★ ルート対象：チェック状態とは無関係に、カート内の全商品
//    → 画面に入った瞬間から「全商品を巡回するルート」を必ず描画する
                            val targetItems = cartItemsInStore

// ★ 本当に商品が 1 件も無い場合だけ、ルートなし
                            if (targetItems.isEmpty()) {
                                routeNodesState = emptyList()
                                sortedAreaOrder = emptyList()
                                sortedCartItems = emptyList()
                                return@LaunchedEffect
                            }


// ★ 商品巡回ルートの算出（入口 → 商品…）
// ※ ルート計算は常に自動最短（エリア順序スライダーは表示順のみ）
                            val routeProducts =
                                navRepo.buildRouteForProducts(
                                    storeId = storeId,
                                    startNodeId = entrance.nodeId,
                                    items = targetItems
                                )

// ★ 現状：レジへ戻る処理は行わない（最後の商品地点で終了する）
                            routeNodesState = routeProducts

                            // ========================================
                            // ★ ルートに基づいてエリアと商品を並び替え
                            // ========================================

                            // 1. 各アクセスポイントがルート上で何番目に訪問されるかを記録（安定版）
                            val apToIndex = mutableMapOf<String, Int>()
                            val layout = storeLayout!!

// ★ route ノードID → 訪問順 index（確実に一致するキー）
                            val nodeIdToIndex = mutableMapOf<String, Int>()
                            routeProducts.forEachIndexed { index, node ->
                                nodeIdToIndex[node.nodeId] = index
                            }

// ★ まず nearestNodeId で確定（最も安定）
//   nearestNodeId が無い場合のみ距離でフォールバック
                            layout.access_points.forEach { ap ->
                                val apId = ap.accessPointId
                                if (apId.isNullOrEmpty()) return@forEach

                                val nearestId = ap.nearestNodeId
                                if (!nearestId.isNullOrEmpty()) {
                                    val idx = nodeIdToIndex[nearestId]
                                    if (idx != null) {
                                        apToIndex[apId] = idx
                                        return@forEach
                                    }
                                }

                                // フォールバック：最も近いノードを採用（閾値比較はしない）
                                var bestIndex = 0
                                var bestDist = Float.MAX_VALUE
                                routeProducts.forEachIndexed { index, node ->
                                    val dx = node.x - ap.x
                                    val dy = node.y - ap.y
                                    val d = kotlin.math.sqrt(dx * dx + dy * dy)
                                    if (d < bestDist) {
                                        bestDist = d
                                        bestIndex = index
                                    }
                                }
                                apToIndex[apId] = bestIndex
                            }

// 2. 商品をルート訪問順序でソート
                            val itemsWithOrder = targetItems.map { item ->
                                val apId = item.accessPointId ?: ""
                                val order = apToIndex[apId] ?: Int.MAX_VALUE
                                item to order
                            }.sortedBy { it.second }

                            sortedCartItems = itemsWithOrder.map { it.first }

// 3. カテゴリ（漢字）をルート訪問順序でソート（★ここが重要）
                            val categoryToMinOrder = mutableMapOf<String, Int>()
                            itemsWithOrder.forEach { (item, order) ->
                                val cat = item.category
                                if (!categoryToMinOrder.containsKey(cat) || categoryToMinOrder[cat]!! > order) {
                                    categoryToMinOrder[cat] = order
                                }
                            }

                            sortedAreaOrder = categoryToMinOrder.entries
                                .sortedBy { it.value }
                                .map { it.key }
                        }



                        // ---- 実際に地図上にルートを描画 ----
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            // 🟦 店舗黒枠の位置（画像内の相対位置で固定）
                            // ★ PNG 全面使用 0〜1 坐标，不再需要黑框区域
                            // ★ 整张图左上角 = (0,0), 右下角 = (1,1)


                            // -----------------------------------------------------
                            // デバッグ用：ノード・エッジ・棚・アクセスポイントを重ねて描画
                            //   ※ 常時表示すると見づらいので、通常は false のままにする
                            // -----------------------------------------------------
                            val debugDraw = false

                            if (debugDraw && storeLayout != null) {
                                // -------------------------------------------------------------
// ★ ノード訪問順序を描画（数字で表示）
// -------------------------------------------------------------
                                if (routeNodesState.isNotEmpty()) {
                                    routeNodesState.forEachIndexed { index, node ->
                                        val px = size.width * node.x
                                        val py = size.height * node.y

                                        drawContext.canvas.nativeCanvas.apply {
                                            val textPaint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.RED
                                                textSize = 28f
                                                isAntiAlias = true
                                            }
                                            drawText(
                                                (index + 1).toString(),
                                                px,
                                                py,
                                                textPaint
                                            )
                                        }
                                    }
                                }

                                val layout = storeLayout!!

                                // 1) 棚矩形を半透明で描画
                                layout.shelves.forEach { shelf ->
                                    // ★ 棚座標：0〜1 → キャンバス全面へ線形変換
                                    val left = size.width * (shelf.x - shelf.width / 2f)
                                    val topRect = size.height * (shelf.y - shelf.height / 2f)
                                    val right = size.width * (shelf.x + shelf.width / 2f)
                                    val bottom = size.height * (shelf.y + shelf.height / 2f)


                                    drawRect(
                                        color = Color(0f, 0f, 0f, 0.15f),
                                        topLeft = Offset(left, topRect),
                                        size = Size(right - left, bottom - topRect)
                                    )
                                }

                                // 2) ノード位置を小さな灰色の点で描画
                                layout.nodes.forEach { node ->
                                    val px = size.width  * node.x
                                    val py = size.height * node.y
                                    drawCircle(
                                        color = Color.DarkGray,
                                        radius = 3.dp.toPx(),
                                        center = Offset(px, py)
                                    )
                                }

                                // 3) nav_edges を細い灰色の線で描画
                                val nodeMap = layout.nodes.associateBy { it.nodeId }
                                layout.edges.forEach { edge ->
                                    val from = nodeMap[edge.fromNodeId]
                                    val to = nodeMap[edge.toNodeId]
                                    if (from != null && to != null) {
                                        // ★ エッジ線分：0〜1 → キャンバス全面へ
                                        val p1 = Offset(
                                            x = size.width * from.x,
                                            y = size.height * from.y
                                        )
                                        val p2 = Offset(
                                            x = size.width * to.x,
                                            y = size.height * to.y
                                        )

                                        drawLine(
                                            color = Color.LightGray,
                                            start = p1,
                                            end = p2,
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                                }

                                // 4) アクセスポイントを青い点で描画
                                layout.access_points.forEach { ap ->
                                    // ★ アクセスポイント座標
                                    val cx = size.width * ap.x
                                    val cy = size.height * ap.y

                                    drawCircle(
                                        color = Color.Blue,
                                        radius = 3.dp.toPx(),
                                        center = Offset(cx, cy)
                                    )
                                }
                            }
                            // -------------------------------------------------------------
                            // ★ デバッグ用：ルートで使用されたエッジの使用回数を集計する
                            // -------------------------------------------------------------
                            val edgeUsage = mutableMapOf<Pair<String,String>, Int>()

                            if (routeNodesState.size > 1) {
                                val nodes = routeNodesState
                                for (i in 0 until nodes.size - 1) {
                                    val a = nodes[i].nodeId
                                    val b = nodes[i+1].nodeId

                                    // 無向グラフなのでIDをソートしてキー化
                                    val key =
                                        if (a < b) a to b else b to a

                                    edgeUsage[key] = (edgeUsage[key] ?: 0) + 1
                                }
                            }


                            // =============================================================
// ★ 使用回数に応じてエッジを描画（圆角路径 + 方向箭头）
// =============================================================
                            if (routeNodesState.size > 1) {

                                val nodes = routeNodesState
                                val cornerRadius = 20.dp.toPx() // 转角圆弧半径

                                for (i in 0 until nodes.size - 1) {

                                    val a = nodes[i]
                                    val b = nodes[i + 1]

                                    // 無向エッジキー
                                    val key =
                                        if (a.nodeId < b.nodeId) a.nodeId to b.nodeId else b.nodeId to a.nodeId

                                    val count = edgeUsage[key] ?: 1

                                    // 使用回数ごとに線の太さを変更
                                    val stroke = when {
                                        count >= 4 -> 10.dp.toPx()
                                        count == 3 -> 7.dp.toPx()
                                        count == 2 -> 5.dp.toPx()
                                        else -> 3.dp.toPx()
                                    }

                                    val p1 = Offset(size.width * a.x, size.height * a.y)
                                    val p2 = Offset(size.width * b.x, size.height * b.y)

                                    // ========================================
                                    // 判断是否为转角（检查前一段和后一段的方向）
                                    // ========================================
                                    val isCorner = if (i > 0 && i < nodes.size - 1) {
                                        val prev = nodes[i - 1]
                                        val curr = nodes[i]
                                        val next = nodes[i + 1]

                                        val prevP = Offset(size.width * prev.x, size.height * prev.y)
                                        val currP = Offset(size.width * curr.x, size.height * curr.y)
                                        val nextP = Offset(size.width * next.x, size.height * next.y)

                                        // 计算两个方向向量
                                        val dx1 = currP.x - prevP.x
                                        val dy1 = currP.y - prevP.y
                                        val dx2 = nextP.x - currP.x
                                        val dy2 = nextP.y - currP.y

                                        // 如果方向改变，则为转角
                                        kotlin.math.abs(dx1 * dx2 + dy1 * dy2) < 0.7f *
                                                kotlin.math.sqrt((dx1*dx1 + dy1*dy1) * (dx2*dx2 + dy2*dy2))
                                    } else false

                                    // 绘制路径（带圆角）
                                    if (isCorner && i < nodes.size - 1) {
                                        // 转角处使用圆角路径
                                        val path = androidx.compose.ui.graphics.Path()

                                        val curr = nodes[i]
                                        val currP = Offset(size.width * curr.x, size.height * curr.y)

                                        // 计算圆角的控制点
                                        val toNext = Offset(p2.x - currP.x, p2.y - currP.y)
                                        val len = kotlin.math.sqrt(toNext.x * toNext.x + toNext.y * toNext.y)

                                        if (len > cornerRadius * 2) {
                                            val ratio = kotlin.math.min(cornerRadius / len, 0.3f)
                                            val controlPoint = Offset(
                                                currP.x + toNext.x * ratio,
                                                currP.y + toNext.y * ratio
                                            )

                                            path.moveTo(p1.x, p1.y)
                                            path.lineTo(currP.x, currP.y)
                                            path.quadraticBezierTo(
                                                currP.x, currP.y,
                                                controlPoint.x, controlPoint.y
                                            )
                                            path.lineTo(p2.x, p2.y)

                                            drawPath(
                                                path = path,
                                                color = Color.Red,
                                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
                                            )
                                        } else {
                                            // 距离太短，直接画直线
                                            drawLine(
                                                color = Color.Red,
                                                start = p1,
                                                end = p2,
                                                strokeWidth = stroke
                                            )
                                        }
                                    } else {
                                        // 非转角，直接画直线
                                        drawLine(
                                            color = Color.Red,
                                            start = p1,
                                            end = p2,
                                            strokeWidth = stroke
                                        )
                                    }

                                    // ★ 中央に "×N" を描く（N ≥ 2 の時だけ）
                                    if (count >= 2) {
                                        val midX = (p1.x + p2.x) / 2f
                                        val midY = (p1.y + p2.y) / 2f

                                        drawContext.canvas.nativeCanvas.apply {
                                            val textPaint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.RED
                                                textSize = 32f
                                                isAntiAlias = true
                                            }
                                            drawText("×${count}", midX, midY, textPaint)
                                        }
                                    }

                                    // ========================================
                                    // 在路径上绘制方向箭头
                                    // ========================================
                                    if (i % 2 == 0 || i == nodes.size - 2) { // 每隔一段或最后一段绘制箭头
                                        val midX = (p1.x + p2.x) / 2f
                                        val midY = (p1.y + p2.y) / 2f

                                        // 计算方向
                                        val dx = p2.x - p1.x
                                        val dy = p2.y - p1.y
                                        val angle = kotlin.math.atan2(dy.toDouble(), dx.toDouble()).toFloat()

                                        // 箭头大小
                                        val arrowSize = 12.dp.toPx()

                                        // 绘制箭头（三角形）
                                        val arrowPath = androidx.compose.ui.graphics.Path()
                                        arrowPath.moveTo(midX, midY)
                                        arrowPath.lineTo(
                                            midX - arrowSize * kotlin.math.cos(angle + kotlin.math.PI / 6).toFloat(),
                                            midY - arrowSize * kotlin.math.sin(angle + kotlin.math.PI / 6).toFloat()
                                        )
                                        arrowPath.lineTo(
                                            midX - arrowSize * kotlin.math.cos(angle - kotlin.math.PI / 6).toFloat(),
                                            midY - arrowSize * kotlin.math.sin(angle - kotlin.math.PI / 6).toFloat()
                                        )
                                        arrowPath.close()

                                        drawPath(
                                            path = arrowPath,
                                            color = Color.Red
                                        )
                                    }
                                }
                            }


                            // =====================================================
                            // 2) カート内商品のアクセスポイントを黒丸で描画
                            //    ※ チェック状態とは無関係に「全商品」を対象とする
                            // =====================================================

                            // ★ カート内の全商品ID（チェック状態は無視）
                            val targetProductIds = cartItemsInStore
                                .map { it.productId }
                                .toSet()

                            // productId -> accessPointId の対応表
                            val productIdToApId = cartItemsInStore.associate { it.productId to it.accessPointId }

                            // 対象 accessPointId の集合
                            val targetApIds = targetProductIds
                                .mapNotNull { pid -> productIdToApId[pid] }
                                .toSet()

                            // レイアウトからアクセス点一覧を取得（null なら空）
                            val accessPoints = storeLayout?.access_points ?: emptyList()

                            // 対象商品のアクセス点にだけ黒丸を描く
                            accessPoints
                                .filter { ap -> ap.accessPointId in targetApIds }
                                .forEach { ap ->
                                    val cx = size.width * ap.x
                                    val cy = size.height * ap.y

                                    // ★ ブラックポイント本体
                                    drawCircle(
                                        color = Color.Black,
                                        radius = 6.dp.toPx(),
                                        center = Offset(cx, cy)
                                    )

// ★ デバッグ時のみ、AP の簡易ラベル(E_L 等)を表示
                                    if (debugDraw) {
                                        val label = ap.accessPointId
                                            ?.removePrefix("AP_")
                                            ?: ""

                                        drawContext.canvas.nativeCanvas.apply {
                                            val textPaint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.BLACK
                                                textSize = 28f
                                                isAntiAlias = true
                                            }
                                            drawText(label, cx + 4f, cy - 4f, textPaint)
                                        }
                                    }
                                }


                        }


                    }

                }
            }

            // ---------------- エリア順序（スライダー + < / > ボタン） ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "エリア順序",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sortedAreaOrder.forEach { id ->
                        val label = categoryAreaLabelMap[id] ?: id
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = label)
                            }
                        }
                    }


                }
            }

            // ---------------- 対象商品（横スクロール / 小さめカード） ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f) // 地図より小さめ
            ) {
                Text(
                    text = "対象商品",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sortedCartItems.forEach { item ->
                        val checked = checkedMap[item.productId] ?: false

                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(90.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                            ) {

                                // ---------------- 商品内容（既存） ----------------
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2
                                    )

                                    Text(
                                        text = "数量：${item.quantity}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "購入",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = { newChecked ->
                                                checkedMap[item.productId] = newChecked
                                            }
                                        )
                                    }
                                }

                                // ---------------- エリア表示（右上） ----------------
                                val areaCode = resolveAreaCode(item)
                                Surface(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    shape = RoundedCornerShape(8.dp),
                                    tonalElevation = 2.dp,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = areaCode,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    // ---------------- エラーダイアログ ----------------
    if (dialogMessage != null) {
        AlertDialog(
            onDismissRequest = {
                dialogMessage = null
            },
            title = { Text("情報") },
            text = { Text(dialogMessage!!) },
            confirmButton = {
                TextButton(
                    onClick = {
                        // ★ エラーの場合のみ使うので、閉じるだけ
                        dialogMessage = null
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}