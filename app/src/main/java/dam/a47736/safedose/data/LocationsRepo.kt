package dam.a47736.safedose.data

object LocationsRepo {

    fun obtainLocations(): List<CommunityCenter> {
        return listOf(
            CommunityCenter("Kosmicare", 3, "Rua Cesário Verde 17B, 1170-283 Lisboa", listOf(ServiceType.DRUG_TESTING), "16:00-21:00", 38.7290487,-9.132108, "kosmicare.png"),
            CommunityCenter("GAT Checkpoint LX", 1, "Tv. Monte do Carmo 2, Lisboa", listOf(ServiceType.IST_TESTING), "12:00-20:00", 38.716518, -9.149944, "checkpoint.jpg"),
            CommunityCenter("GAT IN Mouraria", 2, "Calçada de Santo André 79 e 81-83, 1100-496 Lisboa", listOf(ServiceType.CONSUMMING, ServiceType.IST_TESTING), "14:00-20:00", 38.7156413,-9.1323151, "gatmouraria.jpg"),
            CommunityCenter("GAT Setubal", 4, "Travessa das Lobas nº12, R/C, 2900-444, Setúbal", listOf(ServiceType.IST_TESTING), "Dias úteis das 10:00 às 18:00", 38.5240823, -8.8959143, "gatsetubal.wepp"),
            CommunityCenter("GAT Almada", 5, "R. Luís de Camões 14, 2810-251 Almada", listOf(ServiceType.IST_TESTING), "2ªf a 4ªf  das 9:30 às 15:30", 38.6561374, -9.15246, "gatalmada.jpg")
        )
    }

}