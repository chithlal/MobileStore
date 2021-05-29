package com.chithlal.mobilestore.model

data class Features(
    val exclusions: List<List<Exclusion>>,
    val features: List<Feature>
)

data class Exclusion(
    val feature_id: String,
    val options_id: String
)

data class Feature(
    val feature_id: String,
    val name: String,
    val options: List<Option>
)

data class Option(
    val icon: String,
    val id: String,
    val name: String
)