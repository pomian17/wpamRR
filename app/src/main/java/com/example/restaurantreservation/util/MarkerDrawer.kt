package com.example.restaurantreservation.util

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import com.example.restaurantreservation.R


object MarkerDrawer {
    fun drawMarker(rating: Double, context: Context): Bitmap {
        val width = 80f
        val height = 100f
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)


        val path = Path()
        path.arcTo(RectF(0f, 0f, width, width), 0f, 45f)
        val pathMeasure = PathMeasure(path, false)
        val intersectRightPos = floatArrayOf(0f, 0f)
        pathMeasure.getPosTan(pathMeasure.length, intersectRightPos, null)
        path.lineTo(width / 2, height)
        path.lineTo(width - intersectRightPos[0], intersectRightPos[1])
        path.arcTo(RectF(0f, 0f, width, width), 135f, 225f)
        path.close()


        val paint = Paint()
        paint.color = ArgbEvaluator().evaluate(
            ((rating.toFloat() - 2) / 3).coerceIn(0.0f, 1.0f),
            context.resources.getColor(R.color.rrRed, null),
            context.resources.getColor(R.color.rrGreen, null)
        ) as Int
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)


        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas.drawPath(path, paint)


        paint.color = Color.WHITE
        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL

        val xPos = width / 2
        val yPos = (width / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(rating.toString(), xPos, yPos, paint)

        return bitmap
    }

}