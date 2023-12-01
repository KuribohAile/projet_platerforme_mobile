package com.example.locslspecies.model

data class _User (
    var name: String = "",
    var surname: String = "",
    var description: String = "",
    var hobby: List<String> = emptyList(),
    var imageProfileUrl: String = "",
    var password: String = "",
    var repeatPassword: String = "",
    var email: String = "",
)
