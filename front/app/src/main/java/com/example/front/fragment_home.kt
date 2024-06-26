package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.databinding.FragmentHomeBinding
import com.example.front.databinding.HomeReviewRecyclerviewBinding


data class ReviewObject(
    val ratings: Float,
    val storeTitle: String,
    val reviewContent: String,
    val reviewTime : String,
)
class ReviewViewHolder(val binding: HomeReviewRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)


class ReviewAdapter(val datas: MutableList<ReviewObject>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(review: ReviewObject)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReviewViewHolder(HomeReviewRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ReviewViewHolder).binding
        val review = datas[position]
        binding.homeReviewTime.text = datas[position].reviewTime
        binding.homeReviewStoreName.text = datas[position].storeTitle
        binding.homeReviewStoreText.text = datas[position].reviewContent
        binding.homeReviewRatingbar.rating = datas[position].ratings

        binding.root.setOnClickListener {
            itemClickListener.onItemClick(review)
        }


    }
}




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHome : Fragment(), ReviewAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        val datas = mutableListOf<ReviewObject>()
        for(i in 0..10){
            val data = ReviewObject(
                ratings = (i.toFloat() / 2),
                storeTitle = "몇번째가게 $i",
                reviewContent = "몇번째리뷰 $i",
                reviewTime = "2024-06-18"
            )
            datas.add(data)
        }
        val adapter = ReviewAdapter(datas, this)
        val layoutManager = LinearLayoutManager(activity)
        binding.fragmenthome.layoutManager = layoutManager
        binding.fragmenthome.adapter = adapter

        return binding.root // 바인딩된 레이아웃 반환
    }

    override fun onItemClick(review: ReviewObject) {
        Log.d("ReviewClick", "클릭된 리뷰: ${review.storeTitle}, ${review.reviewContent}, ${review.ratings}, ${review.reviewTime}")
        val intent :Intent = Intent(requireContext(),StoreDetailActivity::class.java).apply {
            putExtra("storeTitle", review.storeTitle)
            putExtra("reviewContent", review.reviewContent)
            putExtra("ratings", review.ratings.toString())
            putExtra("reviewTime", review.reviewTime)
        }
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
