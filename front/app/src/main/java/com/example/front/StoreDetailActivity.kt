package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.front.databinding.ActivityMainBinding
import com.example.front.databinding.ActivityStoreDetailBinding

class StoreDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeTitle = intent?.getStringExtra("storeTitle")
        val reviewContent = intent?.getStringExtra("reviewContent")
        val ratings = intent?.getStringExtra("ratings")
        val reviewTime = intent?.getStringExtra("reviewTime")
        Log.d("value", "$storeTitle $storeTitle $ratings $reviewTime")
        binding.storeTitle.text = storeTitle
        binding.reviewContent.text = storeTitle
        binding.ratings.text = ratings
        binding.reviewTime.text = reviewTime

    }
}