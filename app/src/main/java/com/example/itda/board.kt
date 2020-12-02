package com.example.itda

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_board.*
import java.security.AccessController.getContext


class board : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ask_a_father.setOnClickListener {
            activity?.let {
                val intent = Intent(context, BoardPage::class.java)
                intent.putExtra("BoardPage", "father")
                startActivity(intent)
            }
        }
        ask_a_mother.setOnClickListener {
            activity?.let {
                val intent = Intent(context, BoardPage::class.java)
                intent.putExtra("BoardPage", "mother")
                startActivity(intent)
            }
        }
        ask_a_brother.setOnClickListener {
            activity?.let {
                val intent = Intent(context, BoardPage::class.java)
                intent.putExtra("BoardPage", "brother")
                startActivity(intent)
            }
        }
        ask_a_grandparents.setOnClickListener {
            activity?.let {
                val intent = Intent(context, BoardPage::class.java)
                intent.putExtra("BoardPage", "grandparents")
                startActivity(intent)
            }
        }
        my_post.setOnClickListener {
            activity?.let {
                val intent = Intent(context, MyPostPage::class.java)
                startActivity(intent)
            }
        }
        hot_post.setOnClickListener {
            activity?.let {
                val intent = Intent(context, HotpostPage::class.java)
                startActivity(intent)
            }
        }
        create_board.setOnClickListener {
            activity?.let {
                val intent = Intent(context, RequestNewBoard::class.java)
                startActivity(intent)
            }
        }
    }
}


