package my.significs.gep.faceid

import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

interface OnCompanyClickListener {
    fun onCompanyClick(company: CompanyModel)
    fun onUserClick(user: UserModel)

}