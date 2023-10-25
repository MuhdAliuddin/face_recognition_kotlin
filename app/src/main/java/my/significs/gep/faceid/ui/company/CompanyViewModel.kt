package my.significs.gep.faceid.ui.company

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.significs.gep.faceid.data.Datasource
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

class CompanyViewModel: ViewModel()  {

    private val myDataset = Datasource().loadCompanyList()
    private val TLPUserList: List<UserModel> = listOf()

    private val _companyList = MutableLiveData<List<CompanyModel>>().apply {
        value = myDataset
    }
    private val _userList = MutableLiveData<List<UserModel>>().apply {
        value = TLPUserList
    }
    private val _selectedUser = MutableLiveData<UserModel>().apply {
        value = UserModel("", "", "")
    }
    private val _editStatus = MutableLiveData<Boolean>().apply {
        value = false
    }

    val userList: MutableLiveData<List<UserModel>> = _userList
    val companyList: MutableLiveData<List<CompanyModel>> = _companyList
    val selectedUser: MutableLiveData<UserModel> = _selectedUser
    val editStatus: MutableLiveData<Boolean> = _editStatus

    fun onLoadUsers(companyName: CompanyModel) {
        val matchedUser = findUser(companyName)
        userList.postValue(matchedUser)
    }
    fun onSelectUser(user: UserModel) {
        selectedUser.postValue(user)
    }

    private fun findUser(companySelected: CompanyModel): List<UserModel> {
        return companySelected.usersList
    }

    fun onEditStatusFalse() {
        editStatus.postValue(false)
    }

    fun onEditStatusTrue() {
        editStatus.postValue(true)
    }

}