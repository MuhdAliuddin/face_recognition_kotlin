package my.significs.gep.faceid

import my.significs.gep.faceid.ui.dashboard.DashboardFragment
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel

class SuccessScan {
    companion object {

        fun succ( ) {
            DashboardViewModel.setSuccess()
            // To scroll to the last message
            // See this SO answer -> https://stackoverflow.com/a/37806544/10878733
        }

    }
}