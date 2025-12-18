package com.example.supermarket.utils

import android.content.Context
import com.example.supermarket.R

object ImageUtils {
    /**
     * 根据products表中的image_name字段获取图片资源ID
     * @param imageName 图片名称（如 "apple"，不带.png后缀）
     * @param context Android Context
     * @return 图片资源ID，如果找不到则返回默认logo
     */
    fun getProductImageResource(imageName: String?, context: Context): Int {
        // 如果image_name为空，返回默认logo
        if (imageName.isNullOrBlank()) {
            return R.drawable.logo
        }

        // 清理文件名：去除.png后缀，转小写
        val cleanName = imageName
            .removeSuffix(".png")
            .lowercase()

        // 尝试获取资源ID
        val resourceId = context.resources.getIdentifier(
            cleanName,
            "drawable",
            context.packageName
        )

        // 如果找不到，返回默认logo
        return if (resourceId != 0) resourceId else R.drawable.logo
    }
}