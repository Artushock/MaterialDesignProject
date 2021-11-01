package com.artushock.materialdesignproject.data.model

data class MarsRoverPhotosDTO(
    val photos: MutableList<Photo>
) {
    inner class Photo(
        val id: Int,
        val sol: Int,
        val camera: Camera,
        val img_src: String,
        val earth_date: String,
        val rover: Rover,
    ) {
        fun mapToMarsRoverPhoto(): MarsRoverPhoto{
            val id = this.id
            val url = this.img_src
            val roverName = this.rover.name
            val date = this.earth_date
            return MarsRoverPhoto(id, url, roverName, date)
        }

        inner class Camera(
            val id: Int,
            val name: String,
            val rover_id: Int,
            val full_name: String,
        )


        inner class Rover(
            val id: Int,
            val name: String,
            val landing_date: String,
            val launch_date: String,
            val status: String,
        )
    }
}
