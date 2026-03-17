package com.kmpnewsapp.feature.map

data class IndianCity(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val state: String
)

val INDIAN_CITIES = listOf(
    IndianCity("New Delhi", 28.6139, 77.2090, "Delhi"),
    IndianCity("Mumbai", 19.0760, 72.8777, "Maharashtra"),
    IndianCity("Bangalore", 12.9716, 77.5946, "Karnataka"),
    IndianCity("Chennai", 13.0827, 80.2707, "Tamil Nadu"),
    IndianCity("Kolkata", 22.5726, 88.3639, "West Bengal"),
    IndianCity("Hyderabad", 17.3850, 78.4867, "Telangana"),
    IndianCity("Pune", 18.5204, 73.8567, "Maharashtra"),
    IndianCity("Ahmedabad", 23.0225, 72.5714, "Gujarat"),
    IndianCity("Jaipur", 26.9124, 75.7873, "Rajasthan"),
    IndianCity("Lucknow", 26.8467, 80.9462, "Uttar Pradesh"),
    IndianCity("Chandigarh", 30.7333, 76.7794, "Punjab"),
    IndianCity("Bhopal", 23.2599, 77.4126, "Madhya Pradesh"),
    IndianCity("Patna", 25.6093, 85.1376, "Bihar"),
    IndianCity("Kochi", 9.9312, 76.2673, "Kerala"),
    IndianCity("Guwahati", 26.1445, 91.7362, "Assam"),
    IndianCity("Thiruvananthapuram", 8.5241, 76.9366, "Kerala"),
    IndianCity("Indore", 22.7196, 75.8577, "Madhya Pradesh"),
    IndianCity("Surat", 21.1702, 72.8311, "Gujarat"),
    IndianCity("Nagpur", 21.1458, 79.0882, "Maharashtra"),
    IndianCity("Visakhapatnam", 17.6868, 83.2185, "Andhra Pradesh"),
)
