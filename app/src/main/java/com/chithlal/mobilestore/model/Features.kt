package com.chithlal.mobilestore.model

import java.io.Serializable

data class Features(
    val exclusions: List<List<Exclusion>>,
    val features: List<Feature>
): Serializable

data class Exclusion(
    val feature_id: String,
    val options_id: String
): Serializable

data class Feature(
    val feature_id: String,
    val name: String,
    val options: List<Option>
): Serializable

data class Option(
    val icon: String,
    val id: String,
    val name: String
): Serializable