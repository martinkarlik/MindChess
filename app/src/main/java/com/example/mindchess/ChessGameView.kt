package com.example.mindchess

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.example.mindchess.common.BOARD_TILES
import com.example.mindchess.common.DARK_SQUARE_COLOR
import com.example.mindchess.common.LIGHT_SQUARE_COLOR
import com.example.mindchess.common.toInt
import java.lang.Integer.min
import java.lang.Math.max


private val LOG_TAG = "ChessGameViewTest"


private data class BoardInfo(
    val size: Rect,
    val tiles: Int,
    val tileSize: Int,
    val topLeftPoint: Point,
    val lightSquarePaint: Paint,
    val darkSquarePaint: Paint
)

class ChessGameView(context: Context) : SurfaceView(context), GameEventListener {

    private var gameViewModel: GameViewModel? = null

    private lateinit var boardInfo: BoardInfo

    init {
        holder.addCallback(object : SurfaceHolder.Callback {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun surfaceCreated(holder: SurfaceHolder?) {
                boardInfo = getBoardInfo()
                drawBoard()


            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

        }

        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getBoardInfo() : BoardInfo {
        val size = Rect()
        display?.getRectSize(size)

        val tiles = BOARD_TILES
        val tileSize = min(size.width(), size.height()) / tiles

        val lightSquarePaint = Paint()
        lightSquarePaint.color = LIGHT_SQUARE_COLOR

        val darkSquarePaint = Paint()
        darkSquarePaint.color = DARK_SQUARE_COLOR

        val topLeftPoint = Point()
        topLeftPoint.set(
            (size.width() > size.height()).toInt() * (size.width() - size.height()) / 2,
            (size.height() > size.width()).toInt() * (size.height() - size.width()) / 2
        )




        return BoardInfo(
            size = size,
            tiles = tiles,
            tileSize = tileSize,
            topLeftPoint = topLeftPoint,
            lightSquarePaint = lightSquarePaint,
            darkSquarePaint = darkSquarePaint
        )


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawBoard() {
        if (holder.surface.isValid) {


            val canvas = holder.lockCanvas()

            canvas.drawRGB(120, 200, 120)


            for (file in 0..boardInfo.tiles - 1) {
                for (rank in 0..boardInfo.tiles - 1) {

                    val rect = Rect(boardInfo.topLeftPoint.x + file * boardInfo.tileSize,
                        boardInfo.topLeftPoint.y + rank * boardInfo.tileSize,
                        boardInfo.topLeftPoint.x + (file + 1) * boardInfo.tileSize,
                        boardInfo.topLeftPoint.y + (rank + 1) * boardInfo.tileSize)

                    if ((file + rank) % 2 == 0) {
                        canvas.drawRect(rect, boardInfo.darkSquarePaint)
                    } else {
                        canvas.drawRect(rect, boardInfo.lightSquarePaint)
                    }
                }
            }


            gameViewModel?.pieces?.forEach {
                if (it.image != null) {

                    canvas.drawBitmap(
                        it.image!!,
                        null,
                        coordinateToRect(it.coordinate),
                        null
                    )
                }
            }


            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun coordinateToRect(coordinate: Coordinate) : Rect {

        val left = boardInfo.topLeftPoint.x + boardInfo.tileSize * coordinate.x
        val top = boardInfo.topLeftPoint.y + boardInfo.tileSize * (boardInfo.tiles - coordinate.y - 1)
        val right = left + boardInfo.tileSize
        val bottom = top + boardInfo.tileSize

        return Rect(left, top, right, bottom)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewModelChange(viewModel: GameViewModel) {
        gameViewModel = viewModel
        drawBoard()
    }


}