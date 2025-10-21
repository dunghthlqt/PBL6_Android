package com.demo.pbl6_android.data

import android.content.Context
import com.demo.pbl6_android.data.model.ProvinceJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object VietnamAddressData {
    
    data class Province(val name: String, val districts: List<District>)
    data class District(val name: String, val wards: List<String>)
    
    private var provinces: List<Province> = emptyList()
    private var isInitialized: Boolean = false
    
    fun initialize(context: Context) {
        if (isInitialized) return
        
        try {
            val jsonString = context.assets.open("vietnam-provinces.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val provinceListType = object : TypeToken<List<ProvinceJson>>() {}.type
            val provincesJson: List<ProvinceJson> = gson.fromJson(jsonString, provinceListType)
            
            // Convert JSON data to our data model
            provinces = provincesJson.map { provinceJson ->
                Province(
                    name = provinceJson.name,
                    districts = provinceJson.districts.map { districtJson ->
                        District(
                            name = districtJson.name,
                            wards = districtJson.wards.map { it.name }
                        )
                    }
                )
            }
            
            isInitialized = true
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to hardcoded data if JSON loading fails
            loadFallbackData()
        }
    }
    
    private fun loadFallbackData() {
        provinces = listOf(
            // Miền Bắc
            Province(
                name = "Thành phố Hà Nội",
                districts = listOf(
                    District("Quận Ba Đình", listOf("Phường Cống Vị", "Phường Điện Biên", "Phường Đội Cấn")),
                    District("Quận Hoàn Kiếm", listOf("Phường Hàng Bạc", "Phường Hàng Bài", "Phường Hàng Bồ"))
                )
            ),
            Province(
                name = "Thành phố Hồ Chí Minh",
                districts = listOf(
                    District("Quận 1", listOf("Phường Bến Nghé", "Phường Bến Thành", "Phường Cầu Kho")),
                    District("Quận 3", listOf("Phường 1", "Phường 2", "Phường 3"))
                )
            )
        )
        isInitialized = true
    }
    
    
    fun getProvinces(): List<String> {
        return provinces.map { it.name }
    }
    
    fun getDistricts(provinceName: String): List<String> {
        return provinces.find { it.name == provinceName }?.districts?.map { it.name } ?: emptyList()
    }
    
    fun getWards(provinceName: String, districtName: String): List<String> {
        return provinces.find { it.name == provinceName }
            ?.districts?.find { it.name == districtName }
            ?.wards ?: emptyList()
    }
}
