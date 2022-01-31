package com.mallam.mynotes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mallam.mynotes.databinding.ActivityMainBinding
import com.mallam.mynotes.utilities.Constant
import com.mallam.mynotes.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val notesViewModel: NotesViewModel by lazy {
        NotesViewModel
    }

    private val appUpdate : AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        notesViewModel.init(this)

        registerUpdateListener()
        checkAppUpdate()


        getNotes()
    }

    private fun checkAppUpdate(){
        appUpdate.appUpdateInfo.addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                appUpdate.startUpdateFlowForResult(it, AppUpdateType.FLEXIBLE, this, Constant.REQUEST_CODE_APP_UPDATE)
            }
        }
    }

    private fun registerUpdateListener(){
        appUpdate.registerListener {
            if(it.installStatus() == InstallStatus.DOWNLOADED){
                showUpdateDownloadedSnackBar()
            }
        }
    }

    private fun showUpdateDownloadedSnackBar(){
        Snackbar.make(binding.root, "Update downloaded!", Snackbar.LENGTH_INDEFINITE)
            .setAction("Install"){ appUpdate.completeUpdate() }
            .show()
    }

    override fun onResume() {
        super.onResume()
        appUpdate.appUpdateInfo.addOnSuccessListener{
            if(it.installStatus() == InstallStatus.DOWNLOADED){
                showUpdateDownloadedSnackBar()
            }
        }
    }

    private fun getNotes() {
        notesViewModel.getNotes()
    }

}