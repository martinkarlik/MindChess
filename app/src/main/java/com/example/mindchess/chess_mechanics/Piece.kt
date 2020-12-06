package com.example.mindchess.chess_mechanics

import android.graphics.Bitmap
import com.example.mindchess.Coordinate
import com.example.mindchess.common.toInt

abstract class Piece {

    abstract val team: Int
    abstract var coordinate: Coordinate

    abstract var move_count: Int
    abstract val legal_moves: ArrayList<Coordinate>

    abstract val name: String
    abstract val value: Int


    companion object {
        var last_moved_piece: Piece? = null
    }

    protected fun getPieceSetupMixed(piece_setup: Array<MutableMap<Coordinate, Piece>>) : MutableMap<Coordinate, Piece> {
        val piece_setup_mixed = mutableMapOf<Coordinate, Piece>()
        piece_setup_mixed.putAll(piece_setup[0])
        piece_setup_mixed.putAll(piece_setup[1])

        return piece_setup_mixed
    }


    open fun findPossibleMoves(piece_setup: Array<MutableMap<Coordinate, Piece>>) : Boolean {
        legal_moves.clear()
        return false
    }



    open fun move(piece_setup: Array<MutableMap<Coordinate, Piece>>, destination_coordinate: Coordinate) {

        val team_index = (team < 0).toInt()

        piece_setup[team_index].remove(this.coordinate)
        piece_setup[1 - team_index].remove(destination_coordinate)
        piece_setup[team_index][destination_coordinate] = this



        coordinate = destination_coordinate
        move_count += 1
        last_moved_piece = this
    }

    abstract fun copy() : Piece


}