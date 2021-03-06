package com.rahmat.app.samplecrudkotlin.features.detail

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.rahmat.app.samplecrudkotlin.R
import com.rahmat.app.samplecrudkotlin.databinding.ActivityDetailBinding
import com.rahmat.app.samplecrudkotlin.entity.Student
import com.rahmat.app.samplecrudkotlin.features.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.input_dialog.view.*

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {
    override fun getViewModelBindingVariable(): Int {
        return NO_VIEW_MODEL_BINDING_VARIABLE
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    private lateinit var studentId:String
    lateinit var student: Student

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(getDataBinding().toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        studentId = intent.getLongExtra("student_id", 0).toString()

        viewModel.getStudentbyId().observe(this, Observer {
            student = it!!
            displayStudent(it)
        })

        viewModel.triggerFetchData(studentId)

    }

    private fun displayStudent(student: Student?){
        getDataBinding().layoutDetail.student = student
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> {
                viewModel.deleteData(student)
                Toast.makeText(this, "Data  ${student.name} berhasil dihapus", Toast.LENGTH_LONG).show()
                finish()
            }
            R.id.edit -> {
                val dialogBuilder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.input_dialog, null)
                dialogBuilder.setView(view)
                dialogBuilder.setTitle("Edit Note")
                val etname = view.ed_student_name
                val etnim = view.ed_student_id
                val etdesk = view.ed_student_desk
                val radioGroupGender = view.radio_group_gender
                dialogBuilder.setPositiveButton("Konfirmasi") { _: DialogInterface, _: Int ->
                    val studentName = etname.text
                    val studentNim = etnim.text
                    val studentDesk = etdesk.text
                    val gender: String
                    val selectedRadioButton = radioGroupGender.checkedRadioButtonId
                    gender = when (selectedRadioButton) {
                        R.id.radio_female -> "Pelajaran"
                        else -> "Lainnya"
                    }
                    viewModel.updateData(student.id, studentName.toString(), studentNim.toString(), studentDesk.toString(), gender)
                    Toast.makeText(this, "Note Berhasil diubah $studentName ", Toast.LENGTH_LONG).show()
                }
                dialogBuilder.setNegativeButton("Batal") { _: DialogInterface, _: Int ->
                }
                dialogBuilder.show()
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}
