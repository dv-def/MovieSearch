package ru.dvn.moviesearch.model.staff

enum class Profession(val profession: String) {
    DIRECTOR("Режиссер"),
    PRODUCER("Продюсер"),
    ACTOR("Актер"),
    WRITER("Сценарист"),
    DESIGN("Художник"),
    COMPOSER("Композитор"),
    EDITOR("Монтажер")
}