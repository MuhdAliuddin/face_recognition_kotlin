package my.significs.gep.faceid.data

import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

/**
 * [Datasource] generates a list of [Affirmation]
 */
class Datasource() {

    val TLPUserList: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392"
        ),
        UserModel(
            "AHMAD HAZIM BIN MAHMOD",
            "hazim@gmail.com",
            "F09453229"
        ),
        UserModel(
            "MOHD AMIR BIN SHAH",
            "amir@gmail.com",
            "F0284738"
        ),
    )

    val TLPUserList2: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392"
        ),
        UserModel(
            "AHMAD HAZIM BIN MAHMOD",
            "hazim@gmail.com",
            "F09453229"
        ),
    )

    val TLPUserList3: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392"
        ),
    )

    fun loadCompanyList(): List<CompanyModel> {
        return listOf<CompanyModel>(
            CompanyModel(
                "TLP Sdn Bhd",
                TLPUserList
            ),
            CompanyModel(
                "Innates PLT",
                TLPUserList2
            ),
            CompanyModel(
                "Significs Sdn Bhd",
                TLPUserList3
            ),
        )
    }
    fun loadUserList(): List<UserModel> {
        return listOf<UserModel>(
            UserModel(
                "HANIS HUMAIRA BINTI SUDIR",
                "hanis@gmail.com",
                "F0138392"
                ),
            UserModel(
                "AHMAD HAZIM BIN MAHMOD",
                "hazim@gmail.com",
                "F09453229"
            ),
            UserModel(
                "MOHD AMIR BIN SHAH",
                "amir@gmail.com",
                "F0284738"
            ),
        )
    }
}