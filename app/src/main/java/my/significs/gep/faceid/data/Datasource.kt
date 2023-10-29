package my.significs.gep.faceid.data

import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

/**
 * [Datasource] generates a list of [Affirmation]
 */

data class ResultModel(
    val name : String,
    val email : String,
    val employeeID : String,
    val percentage: String,
)
class Datasource() {

    val ResultListData: List<ResultModel> = listOf<ResultModel>(
        ResultModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392",
            "97%"
        ),
        ResultModel(
            "AHMAD HAZIM BIN MAHMOD",
            "hazim@gmail.com",
            "F09453229",
            "98%"
        ),
        ResultModel(
            "MOHD AMIR BIN SHAH",
            "amir@gmail.com",
            "F0284738",
            "96%"
        ),
    )

    val TLPUserList: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392",
            "233812371892"
        ),
        UserModel(
            "AHMAD HAZIM BIN MAHMOD",
            "hazim@gmail.com",
            "F09453229",
            "233812371892"
        ),
        UserModel(
            "MOHD AMIR BIN SHAH",
            "amir@gmail.com",
            "F0284738",
            "233812371892"
        ),
    )

    val TLPUserList2: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392",
            "233812371892"
        ),
        UserModel(
            "AHMAD HAZIM BIN MAHMOD",
            "hazim@gmail.com",
            "F09453229",
            "233812371892"
        ),
    )

    val TLPUserList3: List<UserModel> = listOf<UserModel>(
        UserModel(
            "HANIS HUMAIRA BINTI SUDIR",
            "hanis@gmail.com",
            "F0138392",
            "233812371892"
        ),
    )

    fun loadResultList(): List<ResultModel> {
        return ResultListData
    }

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