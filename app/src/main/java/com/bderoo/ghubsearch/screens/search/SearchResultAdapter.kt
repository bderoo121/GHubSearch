package com.bderoo.ghubsearch.screens.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.databinding.ListItemSearchRepoBinding
import com.bderoo.ghubsearch.model.Repo

class SearchResultAdapter(private val itemClickListener: (Repo) -> Unit) :
    RecyclerView.Adapter<SearchResultAdapter.RepoViewHolder>() {
    private var results: List<Repo> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setResults(searchResults: List<Repo>) {
        results = searchResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemSearchRepoBinding.inflate(inflater, parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    inner class RepoViewHolder(binding: ListItemSearchRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val repoName: TextView = binding.repoName
        private val repoDescription: TextView = binding.repoDescription
        private val starCount: TextView = binding.repoStarCount
        private val forkCount: TextView = binding.repoForkCount
        private val tags: TextView = binding.repoTags

        fun bind(repo: Repo) {
            repoName.text = repo.name
            repoDescription.text = repo.description
            starCount.text = repo.stargazers_count.toString()
            forkCount.text = repo.forks_count.toString()
            if (repo.topics.isEmpty()) {
                tags.visibility = View.GONE
            } else {
                val formattedTags = repo.topics.joinToString(separator = "  ") { topic ->
                    itemView.context.getString(R.string.bulleted_text, topic)
                }
                tags.text = itemView.context.getString(R.string.tag_list, formattedTags)
                tags.visibility = View.VISIBLE
            }
            itemView.setOnClickListener { itemClickListener(repo) }
        }
    }
}