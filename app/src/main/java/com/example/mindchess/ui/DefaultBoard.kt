package com.example.mindchess.ui

import com.example.mindchess.audio_processing.MoveCommand
import com.example.mindchess.chess_mechanics.Piece

class DefaultBoard(
    val piece_setup: Array<MutableMap<Coordinate, Piece>>
) : Board {

    var last_moved_piece: Piece? = null


    override fun playMove(team: Int, command: MoveCommand) : Boolean {

        var move_played_successfully = false
        var piece: Piece? = null


        if (command.piece != null)
            piece = command.piece
        else if (command.origin_coordinate != null)
            piece = piece_setup[team][command.destination_coordinate]
        else if (command.piece_name != null) {
            for (_piece in piece_setup[team].values) {
                if (command.piece_name == _piece.name && _piece.legal_moves.contains(command.destination_coordinate)) {
                    piece = _piece
                    break
                }
            }
        }

        if (piece != null && piece.legal_moves.contains(command.destination_coordinate)) {
            piece.move(piece_setup, command.destination_coordinate)
            last_moved_piece = piece


            move_played_successfully = true
        }


        return move_played_successfully

    }



    override fun findLegalMoves(team: Int) : Boolean {

        var any_legal_moves = false

        for (piece in piece_setup[team].values) {

            piece.findPossibleMoves(piece_setup, last_moved_piece)
            val illegal_moves = arrayListOf<Coordinate>()

            for (coordinate in piece.legal_moves) {

                val piece_setup_copy = getPieceSetupCopy()
                val piece_copy = piece.copy()


                piece_copy.move(piece_setup_copy, coordinate)
                val last_moved_piece_copy = piece_copy

                for (opposite_piece in piece_setup_copy[1 - team].values) {
                    if (opposite_piece.findPossibleMoves(piece_setup_copy, last_moved_piece_copy)) {
                        illegal_moves.add(coordinate)
                        break
                    }
                }
            }

            piece.legal_moves.removeAll(illegal_moves)


            if (piece.name == "KING") {

                var in_check = false

                for (opposite_piece in piece_setup[1 - team].values) {
                    if (opposite_piece.findPossibleMoves(piece_setup, last_moved_piece)) {
                        in_check = true
                        break
                    }
                }

                for (i in arrayOf(-1, 1)) {
                    if (piece.legal_moves.contains(Coordinate(piece.coordinate.x + i * 2, piece.coordinate.y))) {
                        if (in_check || !piece.legal_moves.contains(Coordinate(piece.coordinate.x + i, piece.coordinate.y))) {
                            piece.legal_moves.remove(Coordinate(piece.coordinate.x + i * 2, piece.coordinate.y))
                        }
                    }
                }
            }




            if (piece.legal_moves.size > 0) {
                any_legal_moves = true
            }
        }

        return any_legal_moves

    }

    override fun isInCheck(team: Int) : Boolean {
        for (piece in piece_setup[1 - team].values) {
            if (piece.findPossibleMoves(piece_setup, last_moved_piece)) {
                return true
            }
        }

        return false
    }




    override fun getPieceSetup(team: Int) : MutableMap<Coordinate, Piece> {
        return piece_setup[team]
    }

    override fun getPieces() : Collection<Piece> {
        val pieces = arrayListOf<Piece>()
        for (team_pieces in piece_setup) {
            pieces.addAll(team_pieces.values)
        }
        return pieces
    }

    override fun getPieceSetupCopy() : Array<MutableMap<Coordinate, Piece>> {
        val copy : Array<MutableMap<Coordinate, Piece>> = arrayOf(mutableMapOf(), mutableMapOf())

        for (team in 0..1) {
            for (piece in piece_setup[team]) {
                copy[team][piece.key.copy()] = piece.value.copy()
            }
        }

        return copy
    }

    override fun getLastMovedPieceCopy() : Piece? {
        return last_moved_piece?.copy()
    }

}
