package pro.sanjagh.lamoa.model

import java.nio.file.Path

case class MovieDetail(
    name: String,
    path: Path,
    year: String,
    types: String = "", // explain that is it a tv series or standalone movie
    quality: String = "", // Get the quality of file
    resolution: String = "", // explain the movie format 1080p, 720p, ...
    language: String = "English"
)
