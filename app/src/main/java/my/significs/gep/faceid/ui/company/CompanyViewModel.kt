package my.significs.gep.faceid.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.significs.gep.faceid.data.Datasource
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

class CompanyViewModel: ViewModel()  {

    val myDataset = Datasource().loadCompanyList()

    val TLPUserList: List<UserModel> = listOf<UserModel>()

    private val _companyList = MutableLiveData<List<CompanyModel>>().apply {
        value = myDataset
    }
    val companyList: MutableLiveData<List<CompanyModel>> = _companyList

    private val _userList = MutableLiveData<List<UserModel>>().apply {
        value = TLPUserList
    }
    val userList: MutableLiveData<List<UserModel>> = _userList

    fun onLoadUsers(companyName: CompanyModel) {
        val matchedUser = findUser(companyName)
        userList.postValue(matchedUser)
    }

    fun findUser(companySelected: CompanyModel): List<UserModel> {
        return companySelected.usersList
    }

}