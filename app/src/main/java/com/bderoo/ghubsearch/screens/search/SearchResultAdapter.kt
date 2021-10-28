package com.bderoo.ghubsearch.screens.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.service.Repo

class SearchResultAdapter(private val itemClickListener: (Repo) -> Unit) :
    RecyclerView.Adapter<SearchResultAdapter.RepoViewHolder>() {
    private var results: List<Repo> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setResults(searchResults: List<Repo>) {
        results = searchResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_search_repo, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repoName: TextView = itemView.findViewById(R.id.repo_name)
        private val repoDescription: TextView = itemView.findViewById(R.id.repo_description)
        private val starCount: TextView = itemView.findViewById(R.id.repo_star_count)
        private val forkCount: TextView = itemView.findViewById(R.id.repo_fork_count)
        private val tags: TextView = itemView.findViewById(R.id.repo_tags)

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