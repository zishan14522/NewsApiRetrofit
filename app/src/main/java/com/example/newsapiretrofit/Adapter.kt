package com.example.newsapiretrofit

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Adapter(private var titles: List<String>,
              private var source: List<String>,
              private var images: List<String>,
              private var links: List<String>,
              private var date: List<String>
): RecyclerView.Adapter<Adapter.ViewHolder>() {



   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_source: TextView=itemView.findViewById(R.id.tv_source)
        val tv_title: TextView=itemView.findViewById(R.id.tv_title)
        val tv_date: TextView=itemView.findViewById(R.id.tv_date)
        val image: ImageView=itemView.findViewById(R.id.image)

       //takes care of click events
       init {
           itemView.setOnClickListener { v: View ->
               val position: Int = adapterPosition
               val intent = Intent(Intent.ACTION_VIEW)
               intent.data = Uri.parse(links[position])
               ContextCompat.startActivity(itemView.context, intent, null)
           }
       }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titles.size
    }
    //Filter For  SearchView
    fun filter(query: String) {
        titles = titles.filter {
            it.contains(query, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_title.text=titles[position]
        holder.tv_source.text=source[position]
        holder.tv_date.text=dateTime(date[position])


        Glide.with(holder.image)
            .load(images[position])
            .into(holder.image)





    }
    fun dateTime(t: String): String? {
        val prettyTime = PrettyTime(Locale(getCountry()))
        var time: String? = null
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH)
            val date: Date = simpleDateFormat.parse(t)
            time = prettyTime.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time
    }

    fun getCountry(): String {
        val locale: Locale = Locale.getDefault()
        val country: String = locale.country.toLowerCase(Locale.ROOT)
        return country
    }

}