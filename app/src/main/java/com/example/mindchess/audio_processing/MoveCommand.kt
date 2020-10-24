package com.example.mindchess.audio_processing

import com.example.mindchess.Coordinate

data class MoveCommand(
    var piece_name: String,
    var origin_coordinate: Coordinate?,
    var destination_coordinate: Coordinate
) : Command