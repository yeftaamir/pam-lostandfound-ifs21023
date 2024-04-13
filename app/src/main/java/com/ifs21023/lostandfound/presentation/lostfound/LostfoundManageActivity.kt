package com.ifs21023.lostandfound.presentation.lostfound

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21023.lostandfound.R
import com.ifs21023.lostandfound.data.model.DelcomLostfound
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.databinding.ActivityLostfoundManageBinding
import com.ifs21023.lostandfound.helper.Utils.Companion.observeOnce
import com.ifs21023.lostandfound.presentation.ViewModelFactory

class LostfoundManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostfoundManageBinding
    private val viewModel by viewModels<LostfoundViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostfoundManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAtion()
    }

    private fun setupView() {
        showLoading(false)
    }

    private fun setupAtion() {
        val isAddLostfound = intent.getBooleanExtra(KEY_IS_ADD, true)
        if (isAddLostfound) {
            manageAddLostfound()
        } else {

            val delcomLostfound = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    intent.getParcelableExtra(KEY_LOST_FOUND, DelcomLostfound::class.java)
                }

                else -> {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<DelcomLostfound>(KEY_LOST_FOUND)
                }
            }

            if (delcomLostfound == null) {
                finishAfterTransition()
                return
            }

            manageEditTodo(delcomLostfound)
        }

        binding.appbarLostfoundManage.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    private fun manageAddTodo() {

        binding.apply {
            appbarLostfoundManage.title = "Tambah Todo"

            btnLostfoundManageSave.setOnClickListener {
                val title = etLostfoundManageTitle.text.toString()
                val description = etLostfoundManageDesc.text.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostfoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }

                observePostLostfound(title, description, status)
            }
        }
    }

    private fun observePostLostfound(title: String, description: String, status: String) {
        viewModel.postLostfound(title, description, status).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)

                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostfoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun manageEditLostfound(Lostfound: DelcomLostfound) {
        binding.apply {
            appbarLostfoundManage.title = "Ubah Data"

            etLostfoundManageTitle.setText(Lostfound.title)
            etLostfoundManageDesc.setText(Lostfound.description)
            etLostfoundManageStatus.setText(Lostfound.status)

            btnLostfoundManageSave.setOnClickListener {
                val title = etLostfoundManageTitle.text.toString()
                val description = etLostfoundManageDesc.text.toString()
                val status = etLostfoundManageStatus.text.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostfoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }

                observePutLostfound(Lostfound.id, title, description, status)
            }
        }
    }

    private fun observePutLostfound(
        LostfoundId: Int,
        title: String,
        description: String,
        status: String,
    ) {
        viewModel.putLostfound(
            LostfoundId,
            title,
            description,
            status
        ).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)
                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostfoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLostfoundManage.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.btnLostfoundManageSave.isActivated = !isLoading

        binding.btnLostfoundManageSave.text =
            if (isLoading) "" else "Simpan"
    }

    companion object {
        const val KEY_IS_ADD = "is_add"
        const val KEY_LOST_FOUND = "todo"
        const val RESULT_CODE = 1002
    }
}