package com.example.restaurantreservation.util

import android.graphics.*
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
import com.example.restaurantreservation.data.model.wpamrr.RestaurantObject
import com.example.restaurantreservation.data.model.wpamrr.RestaurantObjectType
import com.example.restaurantreservation.data.model.wpamrr.RestaurantTable


object RestaurantMapDrawer {
    fun drawRestaurant(restaurantLevel: RestaurantLevel): Bitmap {
        val bitmap = Bitmap.createBitmap(900, 700, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)


        val paint2 = Paint()
        paint2.color = Color.LTGRAY
        paint2.style = Paint.Style.FILL
        canvas.drawPaint(paint2)


        restaurantLevel.shape?.let { drawWalls(it, canvas) }
        restaurantLevel.tables?.let { drawTables(it, canvas) }
        restaurantLevel.objects?.let { drawObjects(it, canvas) }
        return bitmap
    }

    private fun drawWalls(shape: List<Point>, canvas: Canvas) {
        val paint = Paint()
        val walls = getPolygon(shape)

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawPath(walls, paint)

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        canvas.drawPath(walls, paint)

    }

    private fun drawTables(tables: List<RestaurantTable>, canvas: Canvas) {
        val paint = Paint()
        tables.forEach {

            paint.color = Color.RED
            paint.style = Paint.Style.FILL
            canvas.drawRect(
                it.x.toFloat(),
                it.y.toFloat(),
                it.x.toFloat() + it.width,
                it.y.toFloat() + it.height,
                paint
            )

            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            canvas.drawRect(
                it.x.toFloat(),
                it.y.toFloat(),
                it.x.toFloat() + it.width,
                it.y.toFloat() + it.height,
                paint
            )

        }
    }

    private fun drawObjects(restaurantObject: List<RestaurantObject>, canvas: Canvas) {
        val paint = Paint()
        restaurantObject.forEach {

            paint.color = when (it.type) {
                RestaurantObjectType.OTHER -> Color.parseColor("#ff300000")
                RestaurantObjectType.DOOR -> Color.GREEN
                RestaurantObjectType.WINDOW -> Color.BLUE
                RestaurantObjectType.WC -> Color.parseColor("#ffd000d0")
                else -> Color.TRANSPARENT
            }

            it.shape?.let { points ->
                val path = getPolygon(points)

                paint.style = Paint.Style.FILL
                canvas.drawPath(path, paint)

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                canvas.drawPath(path, paint)
            }
        }
    }

    private fun getPolygon(points: List<Point>): Path {
        val path = Path()
        path.moveTo(points.first().x.toFloat(), points.first().y.toFloat())
        for (i in 1..points.lastIndex) {
            path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
        }
        path.close()
        return path
    }

}