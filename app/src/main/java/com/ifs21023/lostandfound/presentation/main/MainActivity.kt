package com.ifs21023.lostandfound.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifs21023.lostandfound.R
import com.ifs21023.lostandfound.adapter.LostfoundAdapter
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundsResponse
import com.ifs21023.lostandfound.data.remote.response.LostfoundsItemResponse
import com.ifs21023.lostandfound.databinding.ActivityMainBinding
import com.ifs21023.lostandfound.helper.Utils.Companion.observeOnce
import com.ifs21023.lostandfound.presentation.Login.LoginActivity
import com.ifs21023.lostandfound.presentation.ViewModelFactory
import com.ifs21023.lostandfound.presentation.lostfound.LostfoundDetailActivity
import com.ifs21023.lostandfound.presentation.lostfound.LostfoundManageActivity
import com.ifs21023.lostandfound.presentation.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LostfoundManageActivity.RESULT_CODE) {
            recreate()
        }

        if (result.resultCode == LostfoundDetailActivity.RESULT_CODE) {
            result.data?.let {
                val isChanged = it.getBooleanExtra(
                    LostfoundDetailActivity.KEY_IS_CHANGED,
                    false
                )

                if (isChanged) {
                    recreate()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        showComponentNotEmpty(false.toString())
        showEmptyError(false)
        showLoading(true)

        binding.appbarMain.overflowIcon =
            ContextCompat
                .getDrawable(this, R.drawable.ic_more_vert_24)

        observeGetLostfounds()
    }

    private fun setupAction() {
        binding.appbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainMenuProfile -> {
                    openProfileActivity()
                    true
                }

                R.id.mainMenuLogout -> {
                    viewModel.logout()
                    openLoginActivity()
                    true
                }

                else -> false
            }
        }

        binding.fabMainAddLostfound.setOnClickListener {
            openAddLostfoundActivity()
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                openLoginActivity()
            } else {
                observeGetLostfounds()
            }
        }
    }

    private fun observeGetLostfounds() {
        viewModel.getLostfounds().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is MyResult.Loading -> {
                        showLoading(true)
                    }

                    is MyResult.Success -> {
                        showLoading(false)
                        loadLostfoundsToLayout(result.data)
                    }

                    is MyResult.Error -> {
                        showLoading(false)
                        showEmptyError(true)
                    }
                }
            }
        }
    }

    private fun loadLostfoundsToLayout(response: DelcomLostfoundsResponse) {
        val lostfounds = response.data.lostfounds
        val layoutManager = LinearLayoutManager(this)
        binding.rvMainLostfounds.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(
            this,
            layoutManager.orientation
        )
        binding.rvMainLostfounds.addItemDecoration(itemDecoration)

        if (lostfounds.isEmpty()) {
            showEmptyError(true)
            binding.rvMainLostfounds.adapter = null
        } else {
            showComponentNotEmpty(true.toString())
            showEmptyError(false)

            val adapter = LostfoundAdapter()
            adapter.submitOriginalList(lostfounds)
            binding.rvMainLostfounds.adapter = adapter
            adapter.setOnItemClickCallback(object : LostfoundAdapter.OnItemClickCallback {
                override fun onCheckedChangeListener(
                    lostfound: LostfoundsItemResponse,
                    isChecked: Boolean
                ) {
                    adapter.filter(binding.svMain.query.toString())

                    viewModel.putLostfound(
                        lostfound.id,
                        lostfound.title,
                        lostfound.description,
                        isChecked
                    ).observeOnce {
                        when (it) {
                            is MyResult.Error -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Gagal menyelesaikan lostfound: " + lostfound.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Gagal batal menyelesaikan lostfound: " + lostfound.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            is MyResult.Success -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Berhasil menyelesaikan lostfound: " + lostfound.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Berhasil batal menyelesaikan lostfound: " + lostfound.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            else -> {}
                        }
                    }
                }

                override fun onClickDetailListener(lostfoundId: Int) {
                    val intent = Intent(
                        this@MainActivity,
                        LostfoundDetailActivity::class.java
                    )
                    intent.putExtra(LostfoundDetailActivity.KEY_LOST_FOUND_ID, lostfoundId)
                    launcher.launch(intent)
                }
            })

            binding.svMain.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        adapter.filter(newText)
                        binding.rvMainLostfounds.layoutManager?.scrollToPosition(0)
                        return true
                    }
                })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbMain.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun openProfileActivity() {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun showComponentNotEmpty(status: String) {
        val isVisible = status.isNotEmpty() // Convert status to boolean based on whether it's empty or not
        binding.svMain.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.rvMainLostfounds.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private fun showEmptyError(isError: Boolean) {
        binding.tvMainEmptyError.visibility =
            if (isError) View.VISIBLE else View.GONE
    }

    private fun openLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun openAddLostfoundActivity() {
        val intent = Intent(
            this@MainActivity,
            LostfoundManageActivity::class.java
        )
        intent.putExtra(LostfoundManageActivity.KEY_IS_ADD, true)
        launcher.launch(intent)
    }
}