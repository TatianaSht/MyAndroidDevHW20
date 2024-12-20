package com.example.myandroiddevhw20.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myandroiddevhw20.data.model.Attraction
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class MapViewModel @Inject constructor() : ViewModel() {

    private val cliff = Attraction("Хабаровский утес",
        "Негласный символ Хабаровска. До основания Хабаровска " +
                "возле утеса жили нанайцы, считающие это место исцеляющим.",
        Point(48.47265504550417, 135.0495032896506)
    )

    private val museumGrodekov = Attraction("Музей Гродекова",
        "Основан 19.04.1894г. как музей Приамурского отдела " +
                "Императорского русского географического общества.",
        Point(48.47347949243925, 135.05059557223527)
    )

    private val museumArt = Attraction("Художественный музей",
        "Крупнейший музей изобразительного искусства на " +
                "Дальнем Востоке.",
        Point(48.47315257052101, 135.05239355666487)
    )

    private val listAttraction = mutableListOf<Attraction>(
        cliff,
        museumGrodekov,
        museumArt
    )

    val listAttractions = listOf(listAttraction).asFlow()

}