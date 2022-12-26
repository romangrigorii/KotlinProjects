package com.example.sevenminuteworkout

class ExerciseModel (
    private var id: Int,
    private var name : String,
    private var image : Int,
    private var isCompleted: Boolean,
    private var isSelected : Boolean
    ){
    fun get() : Int{
        return this.id
    }
    fun set(id: Int){
        this.id = id
    }
    fun getName() : String{
        return this.name
    }
    fun setName(name: String){
        this.name = name
    }
    fun getImage() : Int{
        return this.image
    }
    fun setImage(image: Int){
        this.image = image
    }
    fun getIsCompleted() : Boolean{
        return this.isCompleted
    }
    fun setIsCompleted(isCompleted: Boolean){
        this.isCompleted = isCompleted
    }
    fun getSelected() : Boolean{
        return this.isSelected
    }
    fun setSelected(isSelected: Boolean){
        this.isSelected = isSelected
    }

}