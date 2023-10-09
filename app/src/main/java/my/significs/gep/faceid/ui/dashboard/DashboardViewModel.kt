package my.significs.gep.faceid.ui.dashboard

import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.significs.gep.faceid.Prediction

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

    private val _scanComplete = MutableLiveData<Boolean>().apply {
        value =  false
    }
    var scanComplete: MutableLiveData<Boolean> = _scanComplete

//    fun onScanComplete() {
//        scanComplete.postValue(true)
//    }

    private val _predictionResult = MutableLiveData<Prediction>().apply {
        value =  Prediction(Rect(), "", "")
    }
    var predictionResult: MutableLiveData<Prediction> = _predictionResult

    fun onScanComplete(prediction: Prediction) {
        predictionResult.postValue(prediction)
    }
}
