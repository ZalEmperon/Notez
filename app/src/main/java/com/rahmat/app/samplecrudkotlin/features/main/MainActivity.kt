package com.rahmat.app.samplecrudkotlin.features.main

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmat.app.samplecrudkotlin.R
import com.rahmat.app.samplecrudkotlin.adapter.ItemAdapter
import com.rahmat.app.samplecrudkotlin.databinding.ActivityMainBinding
import com.rahmat.app.samplecrudkotlin.entity.Student
import com.rahmat.app.samplecrudkotlin.features.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override fun getViewModelBindingVariable(): Int {
        return NO_VIEW_MODEL_BINDING_VARIABLE
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private val students : ArrayList<Student> = ArrayList()

    private lateinit var adapter:ItemAdapter

    private val viewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataBinding().itemRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ItemAdapter(students, this)
        getDataBinding().itemRecyclerview.adapter = adapter

        viewModel.getStudents().observe(this, Observer<List<Student>> {
            students.clear()
            students.addAll(it!!)
            adapter.notifyDataSetChanged()
        })

        viewModel.loadStudent()

        fab_add.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.input_dialog, null)
            dialogBuilder.setView(view)
            dialogBuilder.setTitle("Tambah Note Baru")
            val etname = view.ed_student_name
            val etnim = view.ed_student_id
            val etdesk = view.ed_student_desk
            val radioGroupGender = view.radio_group_gender
            dialogBuilder.setPositiveButton("Tambahkan") { _: DialogInterface, _: Int ->
                val studentName = etname.text
                val studentNim = etnim.text
                val studentDesk = etdesk.text
                val gender: String
                val selectedRadioButton = radioGroupGender.checkedRadioButtonId
                Log.v("test", "" + selectedRadioButton)
                gender = when (selectedRadioButton) {
                    R.id.radio_female -> "Pelajaran"
                    else -> "Lainnya"
                }
                viewModel.insertStudent(Student(studentName.toString(), studentNim.toString(), studentDesk.toString(), gender))
                applicationContext.toast("Note Berhasil ditambah")

            }
            dialogBuilder.setNegativeButton("Batal") { _: DialogInterface, _: Int ->
            }
            dialogBuilder.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStudent()

    }

    private fun Context.toast(message:CharSequence){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
