package com.app.mynotes.utilities


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.mynotes.R
import com.app.mynotes.databinding.NotesBottomSheetBinding
import com.app.mynotes.viewmodel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotesBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private var instance: NotesBottomSheet? = null
        private var isNewNote = false
        private var selectedColor = Constant.colorByName[Colors.Blue]

        @JvmStatic
        fun newInstance(isNewNote: Boolean, color: String?): NotesBottomSheet {
            if (instance == null) {
                instance = NotesBottomSheet()
            }
            this.isNewNote = isNewNote
            if (!color.isNullOrEmpty()) selectedColor = color

            return instance!!
        }
    }

    private lateinit var binding: NotesBottomSheetBinding
    private var action = ""
    private var actionColor = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = NotesBottomSheetBinding.inflate(inflater, container, false)

        setNoteColorChecked()
        showDeleteNoteLayout()
        onCLicks()


        return binding.root
    }


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = NotesBottomSheetBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        val param = (binding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior
        if (behavior is BottomSheetBehavior<*>) {
            setBottomSheetCallback(behavior)
        }

    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        broadcastActionIntent()
        action = ""
        actionColor = ""
        selectedColor = Constant.colorByName[Colors.Blue]

    }

    private fun setNoteColorChecked() {
        when(selectedColor){
            Constant.colorByName[Colors.Blue] -> {
                onNoteCheck(1)
            }
            Constant.colorByName[Colors.Red] -> {
                onNoteCheck(2)
            }
            Constant.colorByName[Colors.Yellow] -> {
                onNoteCheck(3)
            }
            Constant.colorByName[Colors.Purple] -> {
                onNoteCheck(4)
            }
            Constant.colorByName[Colors.Green] -> {
                onNoteCheck(5)
            }
            Constant.colorByName[Colors.Orange] -> {
                onNoteCheck(6)
            }
            Constant.colorByName[Colors.Black] -> {
                onNoteCheck(7)
            }
        }
    }

    private fun onNoteCheck(i : Int){
        binding.icCheckNote1.visibility = if(i == 1) View.VISIBLE else View.GONE
        binding.icCheckNote2.visibility = if(i == 2) View.VISIBLE else View.GONE
        binding.icCheckNote3.visibility = if(i == 3) View.VISIBLE else View.GONE
        binding.icCheckNote4.visibility = if(i == 4) View.VISIBLE else View.GONE
        binding.icCheckNote5.visibility = if(i == 5) View.VISIBLE else View.GONE
        binding.icCheckNote6.visibility = if(i == 6) View.VISIBLE else View.GONE
        binding.icCheckNote7.visibility = if(i == 7) View.VISIBLE else View.GONE
    }

    private fun setBottomSheetCallback(behavior: BottomSheetBehavior<*>) {
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun showDeleteNoteLayout() {
        binding.isDeleteNoteLayout = !isNewNote
    }

    private fun onCLicks() {
        onNotesColorClick()
        onImageLayoutClick()
        onWebLinkLayoutClick()
        onDeleteNoteClick()
        onMoreLayoutClick()
    }

    private fun onNotesColorClick() {
        //NOTE 1
        binding.layoutNoteColor1.setOnClickListener {
            setSelectedColor(1)
            onNoteCheck(1)
        }
        //NOTE 2
        binding.layoutNoteColor2.setOnClickListener {
            setSelectedColor(2)
            onNoteCheck(2)
        }
        //NOTE 3
        binding.layoutNoteColor3.setOnClickListener {
            setSelectedColor(3)
            onNoteCheck(3)
        }
        //NOTE 4
        binding.layoutNoteColor4.setOnClickListener {
            setSelectedColor(4)
            onNoteCheck(4)
        }
        //NOTE 5
        binding.layoutNoteColor5.setOnClickListener {
            setSelectedColor(5)
            onNoteCheck(5)
        }
        //NOTE 6
        binding.layoutNoteColor6.setOnClickListener {
            setSelectedColor(6)
            onNoteCheck(6)
        }
        //NOTE 7
        binding.layoutNoteColor7.setOnClickListener {
            setSelectedColor(7)
            onNoteCheck(7)
        }
    }

    private fun setSelectedColor(i: Int) {
        selectedColor = when (i) {
            1 -> {
                actionColor = Colors.Blue.toString()
                Constant.colorByName[Colors.Blue]!!
            }
            2 -> {
                actionColor = Colors.Red.toString()
                Constant.colorByName[Colors.Red]!!
            }
            3 -> {
                actionColor = Colors.Yellow.toString()
                Constant.colorByName[Colors.Yellow]!!
            }
            4 -> {
                actionColor = Colors.Purple.toString()
                Constant.colorByName[Colors.Purple]!!
            }
            5 -> {
                actionColor = Colors.Green.toString()
                Constant.colorByName[Colors.Green]!!
            }
            6 -> {
                actionColor = Colors.Orange.toString()
                Constant.colorByName[Colors.Orange]!!
            }
            7 -> {
                actionColor = Colors.Black.toString()
                Constant.colorByName[Colors.Black]!!
            }
            else -> Constant.colorByName[Colors.Black]!!
        }
    }

    private fun onMoreLayoutClick() {
        binding.layoutMore.setOnClickListener {
            dismiss()
        }
    }

    private fun onImageLayoutClick() {
        binding.layoutAddImage.setOnClickListener {
            action = "Image"
            dismiss()
        }
    }

    private fun onWebLinkLayoutClick() {
        binding.layoutAddURL.setOnClickListener {
            action = "URL"
            dismiss()
        }
    }

    private fun onDeleteNoteClick() {
        binding.layoutDeleteNote.setOnClickListener {
            action = "DeleteNote"
            dismiss()
        }
    }

    private fun broadcastActionIntent(){
        log("Broadcast action: $action")
        log("Broadcast actionColor: $actionColor")
        val intent = Intent("bottom_sheet_action")
        intent.putExtra("action", action)
        intent.putExtra("actionColor", actionColor)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }


}