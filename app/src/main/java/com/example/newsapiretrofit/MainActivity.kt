package com.example.newsapiretrofit

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private var seconds = 3L
    lateinit var countdownTimer: CountDownTimer
    private var titlesList = mutableListOf<String>()
    private var sourceList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()
    private var publishList = mutableListOf<String>()


    //    private lateinit var articles: ArrayList<Article>
    private lateinit var adapter: Adapter

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchNewsData()
    }
    @SuppressLint("SuspiciousIndentation")
    private  fun fetchNewsData() {
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org")
            .build().create<ApiInterface>()

        GlobalScope.launch(Dispatchers.IO){
            try {
//                        val response=retrofit.getHeadLineData(country,apikey)
                val response=retrofit.getNews()
                for (article in response.articles){

                    addToList(article.title,article.source,article.urlToImage,article.url,article.publishedAt)






                    Log.d("mainactivity" , "on result $article")

                }
                //updates ui when data has been retrieved
                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                    fadeIn()
                }
            }catch (e:Exception){
                Log.d("mainActivity" , e.toString())
                withContext(Dispatchers.Main) {
                    attemptRequestAgain(seconds)

                }
            }
        }


    }

    //simple fade in animation for when the app is done loading
    private fun fadeIn() {
        val v_blackScreen : View =findViewById(R.id.v_blackScreen)
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun attemptRequestAgain(seconds: Long) {
        val tv_noInternetCountDown : TextView =findViewById(R.id.tv_noInternetCountDown)

        countdownTimer = object: CountDownTimer(seconds*1010,1000){
            override fun onFinish() {
                fetchNewsData()
                countdownTimer.cancel()
                tv_noInternetCountDown.visibility = View.GONE
                this@MainActivity.seconds+=3

            }
            override fun onTick(millisUntilFinished: Long) {
                tv_noInternetCountDown.visibility = View.VISIBLE
                tv_noInternetCountDown.text = "Cannot retrieve data...\nTrying again in: ${millisUntilFinished/1000}"
                Log.d("MainActivity", "Could not retrieve data. Trying again in ${millisUntilFinished/1000} seconds")
            }
        }
        countdownTimer.start()
    }



    //adds the items to our recyclerview
    private fun addToList(title: String, source: Source, image: String, link: String,date:String) {

        titlesList.add(title)
        sourceList.add(source.name)
        publishList.add(date)
        linksList.add(link)
        imagesList.add(image)


    }

    private fun setUpRecyclerView() {
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= Adapter(titlesList,sourceList,imagesList,linksList,publishList)
    }
}