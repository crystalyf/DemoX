package com.change.demox.views.dialog.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.change.demox.views.dialog.dialogfragment.widget.BottomDialogFragment

/**
 * DialogFragment的 Fragment
 */
class DialogFragmentFragment : Fragment() {

    private var dialogFragment: BottomDialogFragment? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view =
                View.inflate(container!!.context,
                        R.layout.fragment_dialog_fragment, null)
        initView()
        return view
    }

    override fun onDestroyView() {
        dialogFragment = null
        super.onDestroyView()
    }

    private fun initView() {
        dialogFragment = BottomDialogFragment()
        dialogFragment
                ?.setCancelAction {
                    Toast.makeText(activity, "CancelAction", Toast.LENGTH_SHORT).show()
                }
                ?.setOnBackKey {
                    Toast.makeText(activity, "OnBackKey", Toast.LENGTH_SHORT).show()
                    dialogFragment?.dismissAllowingStateLoss()
                }
                ?.setDeleteAction { dialog ->
                    Toast.makeText(activity, "DeleteAction", Toast.LENGTH_SHORT).show()
                }?.show(
                        childFragmentManager,
                        BottomDialogFragment::class.simpleName
                )
    }
}