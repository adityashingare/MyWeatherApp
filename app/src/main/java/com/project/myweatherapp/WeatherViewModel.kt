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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel :  ViewModel() {


    private val weatherApi  =  RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions



    fun getData(city : String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey,city)
                if(response.isSuccessful){
                    Log.i("Response",response.body().toString())
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    Log.i("Error",response.body().toString())
                    _weatherResult.value = NetworkResponse.Error("Failed to load city")

                }
            }catch (E : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load city")
            }

        }

    }

    fun getSuggestions(query: String) {
        viewModelScope.launch {
            val result = fetchSuggestionsFromApi(query)
            _suggestions.value = result
        }
    }

    private suspend fun fetchSuggestionsFromApi(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            // Replace with actual network call to fetch suggestions
            // Example:
            // val response = apiService.getSuggestions(query)
            // response.suggestions
            listOf("New York", "Los Angeles", "Chicago") // Example suggestions, replace with real API response
        }
    }

}