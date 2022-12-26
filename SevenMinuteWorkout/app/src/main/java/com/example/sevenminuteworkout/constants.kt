package com.example.sevenminuteworkout

object constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        exerciseList.add(ExerciseModel(1, "Jumping Jacks", R.drawable.ic_jumping_jacks,false,false))
        exerciseList.add(ExerciseModel(2, "Abdominal Crunch", R.drawable.ic_abdominal_crunch,false,false))
        exerciseList.add(ExerciseModel(3, "High Knees Running in Place", R.drawable.ic_high_knees_running_in_place,false,false))
        exerciseList.add(ExerciseModel(4, "Lunge", R.drawable.ic_lunge,false,false))
        exerciseList.add(ExerciseModel(5, "Plank", R.drawable.ic_plank,false,false))
        exerciseList.add(ExerciseModel(6, "Push Up", R.drawable.ic_push_up,false,false))
        exerciseList.add(ExerciseModel(7, "Push Up and Rotation", R.drawable.ic_push_up_and_rotation,false,false))
        exerciseList.add(ExerciseModel(8, "Side Plank", R.drawable.ic_side_plank,false,false))
        exerciseList.add(ExerciseModel(9, "Squat", R.drawable.ic_squat,false,false))
        exerciseList.add(ExerciseModel(10, "Step Up Into Chair", R.drawable.ic_step_up_onto_chair,false,false))
        exerciseList.add(ExerciseModel(11, "Triceps dip on chair", R.drawable.ic_triceps_dip_on_chair,false,false))
        exerciseList.add(ExerciseModel(12, "Wall Sit", R.drawable.ic_wall_sit,false,false))
        return exerciseList
    }
}