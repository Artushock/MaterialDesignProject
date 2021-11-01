package com.artushock.materialdesignproject.data.model

sealed class MarsRoverPhotosData{
    data class Success(val marsRoverPhotos: MarsRoverPhotosDTO): MarsRoverPhotosData()
    data class Error(val error: Throwable): MarsRoverPhotosData()
    data class Loading (val progress: Int?): MarsRoverPhotosData()
}
