package rr.opencommunity.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import rr.opencommunity.R
import rr.opencommunity.home.map.model.Thread


class NewThreadDialog : DialogFragment() {

    companion object {
        fun newInstance() = NewThreadDialog()
    }

    private lateinit var viewModel: MapsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.thread_fragment, container, false)
        val postButton: Button = view.findViewById(R.id.button)
        val gpsSwitch: Switch = view.findViewById(R.id.switch1)
        val accessControlSwitch: Switch = view.findViewById(R.id.switch2)
        val typeSwitch: Switch = view.findViewById(R.id.switch3)
        val expirationSpinner: Spinner = view.findViewById(R.id.spinner)
        val radiusSpinner: Spinner = view.findViewById(R.id.spinner2)
        val messageEditText: EditText = view.findViewById(R.id.editText2)

        // populate thread expiration hours spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hours,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            expirationSpinner.adapter = adapter
        }

        //populate thread radius miles spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.miles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            radiusSpinner.adapter = adapter
        }

        postButton.setOnClickListener {
            val isGpsExact = gpsSwitch.isChecked
            val isPublic = accessControlSwitch.isChecked
            val isInfoType = typeSwitch.isChecked
            val expirationHours = (expirationSpinner.selectedItem as? String?)?.toInt() ?: 1
            val radiusMiles = (radiusSpinner.selectedItem as? String?)?.toInt() ?: 1
            val message = messageEditText.text.toString()

            val thread = Thread(
                radiusMiles,
                viewModel.getThreadLocation(),
                isGpsExact,
                isPublic,
                isInfoType,
                expirationHours,
                message
            )
            viewModel.uploadThread(thread)
            Toast.makeText(activity, "posted thread: ${viewModel.thread.value?.message}", Toast.LENGTH_LONG).show()
            dismiss()
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MapsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
