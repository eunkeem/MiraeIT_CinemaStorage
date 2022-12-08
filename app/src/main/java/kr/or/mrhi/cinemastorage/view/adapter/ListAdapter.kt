package kr.or.mrhi.cinemastorage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.data.cinema.Cinema
import kr.or.mrhi.cinemastorage.databinding.AdapterListBinding

class ListAdapter(
    private var cinemaList: List<Cinema>,
    private val onCinemaClick: (cinema: Cinema) -> Unit
) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: AdapterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Cinema) {
            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w342${data.poster}")
                .into(binding.ivPoster)

            /*invoke함수로 람다 호출*/
            itemView.setOnClickListener { onCinemaClick.invoke(data) }
        }
    }

    /*api로 받은 영화 정보를 리스트에 업데이트*/
    fun updateCinema(cinemaList: List<Cinema>) {
        this.cinemaList = cinemaList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = AdapterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(cinemaList[position])
    }

    override fun getItemCount(): Int = cinemaList.size
}