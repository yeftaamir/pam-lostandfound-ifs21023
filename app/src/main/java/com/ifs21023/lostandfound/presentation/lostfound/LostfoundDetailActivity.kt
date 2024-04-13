package com.ifs21023.lostandfound.presentation.lostfound

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21023.lostandfound.data.model.DelcomLostfound
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.databinding.ActivityLostfoundDetailBinding
import com.ifs21023.lostandfound.helper.Utils.Companion.observeOnce
import com.ifs21023.lostandfound.presentation.ViewModelFactory

class LostfoundDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostfoundDetailBinding
    private val viewModel by viewModels<LostfoundViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isChanged: Boolean = false

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LostfoundManageActivity.RESULT_CODE) {
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostfoundDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        showComponent(false.toString())
        showLoading(false)
    }

    private fun setupAction() {
        val lostfoundId = intent.getIntExtra(KEY_LOST_FOUND_ID, 0)
        if (lostfoundId == 0) {
            finish()
            return
        }

        observeGetLostfound(lostfoundId)

        binding.appbarLostfoundDetail.setNavigationOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_IS_CHANGED, isChanged)
            setResult(RESULT_CODE, resultIntent)
            finishAfterTransition()
        }
    }

    private fun observeGetLostfound(lostfoundId: Int) {
        viewModel.getLostfound(lostfoundId).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)
                    loadLostfound(result.data.data.lostFound)
                }

                is MyResult.Error -> {
                    Toast.makeText(
                        this@LostfoundDetailActivity,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                    finishAfterTransition()
                }
            }
        }
    }

    private fun loadLostfound(lostfound: LostfoundResponse) {
        showComponent(true.toString())

        binding.apply {
            tvLostfoundDetailTitle.text = lostfound.title
            tvLostfoundDetailDate.text = "Dibuat pada: ${lostfound.createdAt}"
            tvLostfoundDetailDesc.text = lostfound.description

            cbLostfoundDetailIsCompleted.isCompleted = lostfound.isCompleted == 1

            cbLostfoundDetailIsCompleted.setOnCheckedChangeListener { _, isCompleted ->
                viewModel.putLostfound(
                    lostfound.id,
                    lostfound.title,
                    lostfound.description,
                    isCompleted
                ).observeOnce {
                    when (it) {
                        is MyResult.Error -> {
                            if (isCompleted) {
                                Toast.makeText(
                                    this@LostfoundDetailActivity,
                                    "Gagal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LostfoundDetailActivity,
                                    "Gagal batal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is MyResult.Success -> {
                            if (isCompleted) {
                                Toast.makeText(
                                    this@LostfoundDetailActivity,
                                    "Berhasil menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@LostfoundDetailActivity,
                                    "Berhasil batal menyelesaikan todo: " + lostfound.title,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            if ((lostfound.isFinished == 1) != isCompleted) {
                                isChanged = true
                            }
                        }

                        else -> {}
                    }
                }
            }

            ivLostfoundDetailActionDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this@LostfoundDetailActivity)

                builder.setTitle("Konfirmasi Hapus Todo")
                    .setMessage("Anda yakin ingin menghapus todo ini?")

                builder.setPositiveButton("Ya") { _, _ ->
                    observeDeleteLostfound(lostfound.id)
                }

                builder.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog
                }

                val dialog = builder.create()
                dialog.show()
            }

            ivLostfoundDetailActionEdit.setOnClickListener {
                val delcomLostfound = DelcomLostfound(
                    lostfound.id,
                    lostfound.title,
                    lostfound.description,
                    lostfound.isFinished == 1,
                    lostfound.cover,
                    lostfound.isMe,
                    lostfound.status
                )

                val intent = Intent(
                    this@LostfoundDetailActivity,
                    LostfoundManageActivity::class.java
                )
                intent.putExtra(LostfoundManageActivity.KEY_IS_ADD, false)
                intent.putExtra(LostfoundManageActivity.KEY_LOST_FOUND, delcomLostfound)
                launcher.launch(intent)
            }
        }
    }

    private fun observeDeleteLostfound(lostfoundId: Int) {
        showComponent(false.toString())
        showLoading(true)
        viewModel.deleteLostfound(lostfoundId).observeOnce {
            when (it) {
                is MyResult.Error -> {
                    showComponent(true.toString())
                    showLoading(false)
                    Toast.makeText(
                        this@LostfoundDetailActivity,
                        "Gagal menghapus todo: ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MyResult.Success -> {
                    showLoading(false)

                    Toast.makeText(
                        this@LostfoundDetailActivity,
                        "Berhasil menghapus todo",
                        Toast.LENGTH_SHORT
                    ).show()

                    val resultIntent = Intent()
                    resultIntent.putExtra(KEY_IS_CHANGED, true)
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLostfoundDetail.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showComponent(status: String) {
        val isVisible = status.isNotEmpty() // Convert status to boolean based on whether it's empty or not
        binding.llLostfoundDetail.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    companion object {
        const val KEY_LOST_FOUND_ID = "lost_found_id"
        const val KEY_IS_CHANGED = "is_changed"
        const val RESULT_CODE = 1001
    }
}