package my.significs.gep.faceid.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.significs.gep.faceid.User

class DashboardViewModel : ViewModel() {

    companion object {
        private var successLogin : Boolean = false

        fun setSuccess() {
            successLogin = true
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _showDialog = MutableLiveData<Boolean>().apply {
        value =  false
    }
    var showDialog: MutableLiveData<Boolean> = _showDialog


    private val _predictionResult = MutableLiveData<User>().apply {
        value =  User(false, "", "", "")
    }
    var predictionResult: MutableLiveData<User> = _predictionResult

    private val _removeMask = MutableLiveData<Boolean>().apply {
        value =  false
    }
    var removeMask: MutableLiveData<Boolean> = _removeMask

    fun onScanComplete(user: User) {
        predictionResult.postValue(user)
    }

    fun onClearScan() {
        predictionResult.postValue(User(false, "", "", ""))
    }

    fun onShowDialog(isShow: Boolean) {
        showDialog.postValue(isShow)
    }

    fun onRemoveMaskTrue() {
        removeMask.postValue(true)
    }

    fun onRemoveMaskFalse() {
        removeMask.postValue(false)
    }
}
