package com.example.movieapp

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Shader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Path
import com.squareup.picasso.Transformation

class RoundCornerTransformationPicasso(private val radius: Float, private val borderWidth: Float, private val borderColor: Int): Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val path = Path()
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)

        canvas.drawPath(path, paint)

        val borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.color = borderColor
        canvas.drawPath(path, borderPaint)

        val halfBorderWidth = borderWidth / 2
        val borderRect = RectF(halfBorderWidth, halfBorderWidth, width.toFloat() - halfBorderWidth, height.toFloat() - halfBorderWidth)
        canvas.drawRoundRect(borderRect, radius, radius, borderPaint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "round_corner_$radius"
    }

}