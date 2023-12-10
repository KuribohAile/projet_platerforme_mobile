package com.example.locslspecies.model

data class _User (
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var description: String = "",
    var hobby: List<String> = emptyList(),
    var imageProfileUrl: String = "https://avatoon.me/wp-content/uploads/2021/09/Cartoon-Pic-Ideas-for-DP-Profile-10.png",
    var password: String = "",
    var repeatPassword: String = "",
    var email: String = "",
)
