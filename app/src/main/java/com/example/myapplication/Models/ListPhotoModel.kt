package com.example.myapplication.Models

class ListPhotoModel {
    var imageURL: String? = null
    var imageName: String? = null
    var imageTime: String? = null
    var imageType: String? = null
    var imageIngredients: String? = null
    var imagePrep: String? = null

    constructor(imageURL: String?, imageName: String?, imageTime: String?, imageType: String?, imageIngredients: String?, imagePrep: String?) {
        this.imageURL = imageURL
        this.imageName = imageName
        this.imageTime = imageTime
        this.imageType = imageType
        this.imageIngredients = imageIngredients
        this.imagePrep = imagePrep
    }

    constructor() {}
}