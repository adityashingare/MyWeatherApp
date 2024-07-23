package com.project.myweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myweatherapp.api.Constant
import com.project.myweatherapp.api.NetworkResponse
import com.project.myweatherapp.api.RetrofitInstance
import com.project.myweatherapp.api.WeatherModel
import com.project.myweatherapp.api.CitySuggestionModel
import com.project.myweatherapp.api.ExtraCitySuggestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    private val _suggestions = MutableLiveData<List<CitySuggestionModel>>()
    val suggestions: LiveData<List<CitySuggestionModel>> = _suggestions

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    Log.i("Response", response.body().toString())
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    Log.i("Error", response.body().toString())
                    _weatherResult.value = NetworkResponse.Error("Failed to load city")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to load city")
            }
        }
    }

    fun getSuggestions(query: String) {
        viewModelScope.launch {
            val apiSuggestions = fetchSuggestionsFromA pi(query)
            val additionalSuggestions = listOf(CitySuggestionModel(name = "Koparkhairane", region = "Maharashtra", country = "India", id = 56))
            val allSuggestions = apiSuggestions + additionalSuggestions
            _suggestions.value = allSuggestions
        }
    }

    private suspend fun fetchSuggestionsFromApi(query: String): List<CitySuggestionModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.getCitySuggestions(Constant.apiKey, query)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

