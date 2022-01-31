package com.mallam.mynotes.ui


import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.mallam.mynotes.R
import com.mallam.mynotes.data.Note
import com.mallam.mynotes.databinding.DialogBackPressedBinding
import com.mallam.mynotes.databinding.FragmentCreateNoteBinding
import com.mallam.mynotes.utilities.*
import com.mallam.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.collections.ArrayList

class CreateNoteFragment : Fragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    private val binding: FragmentCreateNoteBinding by lazy {
        FragmentCreateNoteBinding.inflate(layoutInflater)
    }
    private val notesViewModel: NotesViewModel by lazy {
        NotesViewModel
    }
    private var note: Note? = null
    private var currentDate: Long? = null
    private var selectedColor = Constant.colorByName[Colors.Blue]
    private var selectedImagePath = ""
    private var webLink = ""
    private var isNewNote = false
    private var noteAdapterPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        notesViewModel.init(requireContext())
        initBindingVariables()

        getArgumentsData()
        setNoteBinding()
        onClicks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBroadcastReceiver()
    }

    private fun initBindingVariables() {
        binding.isLayoutImage = false
        binding.isLayoutWebLink = false
        binding.isLayoutEditWebLink = false
    }

    private fun getArgumentsData() {
        isNewNote = requireArguments().getBoolean("isNewNote")
        if (!isNewNote) {
            note = requireArguments().getSerializable("note") as Note
            noteAdapterPosition = requireArguments().getInt("position")
        }
    }

    private fun setNoteBinding() {
        note?.let {
            //COLOR
            selectedColor = note?.color!!
            //DATE
            currentDate = note?.date!!
            //TEXT
            setTextsBinding()
            //IMAGE
            setImageBinding()
            //WEB LINK
            setWebLinkBinding()
        }
        //COLOR
        setColorViewBinding(selectedColor)
        //DATE
        setDateBinding(currentDate)
    }

    private fun setColorViewBinding(color: String?) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_color_view)
        drawable!!.setTint(Color.parseColor(color!!))
        binding.colorView.background = drawable
    }

    private fun setTextsBinding() {
        binding.etNoteTitle.setText(note!!.title)
        binding.etNoteDescription.setText(note!!.description)
    }

    private fun setDateBinding(dateLong: Long?) {
        if (dateLong == null) {
            val date = Date()
            val pattern = getDatePattern(date)
            var dateStr = convertDateToString(date, pattern)
            if (dateStr.length == 5) {
                val temp = dateStr
                dateStr = "Today, $temp"
            }
            binding.tvDateTime.text = "Edited $dateStr"
            currentDate = Date().time
        } else {
            val pattern = getDatePattern(Date(dateLong))
            var dateStr = convertDateLongToString(dateLong, pattern)
            if (dateStr.length == 5) {
                val temp = dateStr
                dateStr = "Today, $temp"
            }
            binding.tvDateTime.text = "Edited $dateStr"
            currentDate = dateLong
        }
    }

    private fun setImageBinding() {
        if (!note!!.imgPath.isNullOrEmpty()) {
            selectedImagePath = note!!.imgPath!!
            binding.imgNote.setImageBitmap(BitmapFactory.decodeFile(note?.imgPath))
            binding.isLayoutImage = true
        } else {
            binding.isLayoutImage = false
        }
    }

    private fun setWebLinkBinding() {
        if (!note!!.webLink.isNullOrEmpty()) {
            webLink = note!!.webLink!!
            binding.tvWebLink.text = note!!.webLink
            binding.etWebLink.setText(note!!.webLink)
            binding.isLayoutWebLink = true
        } else {
            binding.isLayoutWebLink = false
        }
    }

    private fun registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver,
            IntentFilter("bottom_sheet_action")
        )
    }

    private fun onClicks() {
        onWebButtonsClick()
        onTvWebLinkClick()
        onIcDeleteImageClick()
        onIcDeleteWebClick()
        onIcDoneClick()
        onIcBack()
        onIcMore()
        onColorViewClick()
        onEditWebLinkClick()
        onBackClicked()
    }

    private fun onIcDeleteImageClick() {
        binding.icDeleteImageNote.setOnClickListener {
            selectedImagePath = ""
            binding.isLayoutImage = false
        }
    }

    private fun onWebButtonsClick() {
        //btn OK
        binding.btnOkEditWebLink.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()) {
                validateWebLink()
            } else {
                //requireView().makeToast("Url is Required")
                requireView().makeWarningToasty("Url is Required")
            }
        }
        //btn Cancel
        binding.btnCancelEditWebLink.setOnClickListener {
            binding.isLayoutEditWebLink = false
            binding.isLayoutWebLink = webLink.isNotEmpty()

        }

    }

    private fun onIcDeleteWebClick() {
        binding.icDeleteWebLink.setOnClickListener {
            webLink = ""
            binding.isLayoutWebLink = false
            binding.isLayoutEditWebLink = false
        }
    }

    private fun onTvWebLinkClick() {
        binding.tvWebLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webLink)))
        }
    }

    private fun onIcDoneClick() {
        binding.icDone.setOnClickListener {
            if (note != null) {
                updateNote()
            } else {
                addNote()
            }
        }
    }

    private fun onIcBack() {
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackClicked() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
    }

    private fun onBackPressed() {
        if (matchedNote()) {
            gotoNotesFragment()
        } else {
            alertDialog()

        }
    }

    private fun gotoNotesFragment() {
        Navigation.findNavController(requireView()).run {
            navigate(R.id.action_createNoteFragment_to_notesFragment)
        }
    }

    private fun alertDialog() {
        val dialog = Dialog(requireContext())
        val bindingAlertDialog =
            DialogBackPressedBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(bindingAlertDialog.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        bindingAlertDialog.tvYes.setOnClickListener {
            dialog.dismiss()
            gotoNotesFragment()
        }

        bindingAlertDialog.tvNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun onIcMore() {
        binding.layoutMore.setOnClickListener {
            //BottomSheet
            showBottomSheet()
        }
    }

    private fun onColorViewClick() {
        binding.colorView.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun onEditWebLinkClick() {
        binding.icEditWebLink.setOnClickListener {
            binding.etWebLink.setText(webLink)
            binding.isLayoutEditWebLink = true

        }
    }

    private fun showBottomSheet() {
        val notesBottomSheet = NotesBottomSheet.newInstance(isNewNote, selectedColor)
        notesBottomSheet.show(
            requireActivity().supportFragmentManager,
            "Note Bottom Sheet Fragment"
        )
    }

    private fun getNote(id: Int?) = Note(
        id,
        binding.etNoteTitle.text.toString(),
        currentDate,
        binding.etNoteDescription.text.toString(),
        selectedImagePath,
        webLink,
        selectedColor,
    )

    private fun matchedNote(): Boolean {
        var isMatched = false
        if (note != null) {
            if (note!!.title.equals(binding.etNoteTitle.text.toString().trim()) &&
                note!!.description.equals(binding.etNoteDescription.text.toString().trim()) &&
                note!!.imgPath.equals(selectedImagePath) &&
                note!!.webLink.equals(webLink) &&
                note!!.color.equals(selectedColor)
            ) {
                isMatched = true
            }
        }

        return isMatched
    }

    private fun addNote() {
        if (binding.etNoteTitle.text.toString().trim().isEmpty())
            requireView().makeWarningToasty("Note Title is Required !")
        else if (binding.etNoteDescription.text.toString().trim().isEmpty())
            requireView().makeWarningToasty("Note Description is Required !")
        else if (binding.isLayoutEditWebLink!!)
            requireView().makeWarningToasty("Web URL not confirmed !")
        else {
            note = getNote(null)
            notesViewModel.insertNoteAndRefresh(note!!, requireView())
            isNewNote = false
        }
    }

    private fun updateNote() {
        if (binding.etNoteTitle.text.toString().trim().isEmpty())
            requireView().makeWarningToasty("Note Title is Required !")
        else if (binding.etNoteDescription.text.toString().trim().isEmpty())
            requireView().makeWarningToasty("Note Description is Required !")
        else if (binding.isLayoutEditWebLink!!)
            requireView().makeWarningToasty("Web URL not confirmed !")
        else if (matchedNote()) {
            requireView().makeSuccessToasty("Saved!")
            log("matched Note")
        } else {
            note = getNote(note?.id)
            currentDate = Date().time
            note?.date = currentDate
            notesViewModel.updateNoteAndRefresh(note!!, requireView())
            setDateBinding(currentDate)
        }
    }

    private fun deleteNote() {
        notesViewModel.deleteNoteAndRefresh(note?.id!!, requireActivity(), requireView())
    }

    private fun validateWebLink() {
        if (Patterns.WEB_URL.matcher(etWebLink.text.toString().trim()).matches()) {
            webLink = etWebLink.text.toString().trim()
            binding.tvWebLink.text = webLink
            binding.isLayoutEditWebLink = false
        } else {
            binding.isLayoutEditWebLink = true
            requireView().makeWarningToasty("URL is not valid")
        }
    }

    private fun hasReadStoragePermission() = EasyPermissions.hasPermissions(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun readStorageTask() {
        if (hasReadStoragePermission())
            pickImageFromGallery()
        else
            requestReadStoragePermission()
    }

    private fun requestReadStoragePermission() {
        EasyPermissions.requestPermissions(
            requireActivity(),
            getString(R.string.storage_permission_text),
            Constant.REQUEST_CODE_READ_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultPickImage.launch(intent)
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToNext()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }

        return filePath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            requireActivity()
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        log("PermissionsGranted ** requestCode: $requestCode")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        log("PermissionsDenied ** requestCode: $requestCode")
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        log("onRationaleAccepted ** requestCode: $requestCode")
    }

    override fun onRationaleDenied(requestCode: Int) {
        log("onRationaleDenied ** requestCode: $requestCode")
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1!!.getStringExtra("action")
            val actionColor = p1.getStringExtra("actionColor")
            log("Receiver action: $action")
            log("Receiver actionColor: $actionColor \n**************")
            when (action!!) {
                "Image" -> {
                    readStorageTask()
                }
                "URL" -> {
                    binding.isLayoutWebLink = true
                    binding.isLayoutEditWebLink = true
                }
                "DeleteNote" -> {
                    deleteNote()
                }
                else -> {
                    if (selectedImagePath.isEmpty()) {
                        binding.isLayoutImage = false
                    }
                    if (webLink.isEmpty()) {
                        binding.isLayoutWebLink = false
                    }

                }

            }
            selectedColor = when (actionColor) {
                Colors.Blue.toString() -> Constant.colorByName[Colors.Blue]
                Colors.Red.toString() -> Constant.colorByName[Colors.Red]
                Colors.Yellow.toString() -> Constant.colorByName[Colors.Yellow]
                Colors.Purple.toString() -> Constant.colorByName[Colors.Purple]
                Colors.Green.toString() -> Constant.colorByName[Colors.Green]
                Colors.Orange.toString() -> Constant.colorByName[Colors.Orange]
                Colors.Black.toString() -> Constant.colorByName[Colors.Black]
                //else -> if (note != null) note!!.color else Constant.colorByName[Colors.Blue]
                else -> selectedColor
            }
            setColorViewBinding(selectedColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        log("onDestroy broadcastReceiver")
    }

    private val activityResultPickImage: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                val selectedImageUrl = it
                try {
                    val inputStream =
                        requireActivity().contentResolver.openInputStream(selectedImageUrl)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imgNote.setImageBitmap(bitmap)
                    selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    binding.isLayoutImage = true
                } catch (e: Exception) {
                    log("getPathFromUri Error: ${e.message}")
                    requireView().makeToast("${e.message}")
                    binding.isLayoutImage = false
                }
            }
        }

}