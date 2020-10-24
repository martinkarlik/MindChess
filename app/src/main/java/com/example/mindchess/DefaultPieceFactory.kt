package com.example.mindchess

import com.example.mindchess.chess_mechanics.*
import com.example.mindchess.chess_mechanics.Piece

class DefaultPieceFactory(
    private val team: Int,
    private val image_provider: PieceImageProvider
) : PieceFactory {

    override fun createPawn(coordinate: Coordinate) : Pawn {
        return Pawn(team = team, coordinate = coordinate, image = image_provider.pawn_bitmap)
    }

    override fun createKnight(coordinate: Coordinate) : Knight {
        return Knight(team = team, coordinate = coordinate, image = image_provider.knight_bitmap)
    }

    override fun createBishop(coordinate: Coordinate) : Bishop {
        return Bishop(team = team, coordinate = coordinate, image = image_provider.bishop_bitmap)
    }

    override fun createRook(coordinate: Coordinate) : Rook {
        return Rook(team = team, coordinate = coordinate, image = image_provider.rook_bitmap)
    }

    override fun createQueen(coordinate: Coordinate) : Queen {
        return Queen(team = team, coordinate = coordinate, image = image_provider.queen_bitmap)
    }

    override fun createKing(coordinate: Coordinate) : King {
        return King(team = team, coordinate = coordinate, image = image_provider.king_bitmap)
    }

}