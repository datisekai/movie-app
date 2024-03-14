package com.example.movieapp

import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentHistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private fun generateDataList(): List<PaymentHistoryItem> {
        val dataList: MutableList<PaymentHistoryItem> = ArrayList()
        dataList.add(PaymentHistoryItem(1, "Thanh toán gói Premium1", "23-09-2022", 100000))
        dataList.add(PaymentHistoryItem(2, "Thanh toán gói Premium2", "09-03-2022", 200000))
        dataList.add(PaymentHistoryItem(3, "Thanh toán gói Premium3", "02-10-2022", 300000))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }
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
        val view = inflater.inflate(R.layout.fragment_payment_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(1, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(view.context, 1)

        val dataList: List<PaymentHistoryItem>? = generateDataList() // Tạo danh sách dữ liệu

        val adapter = dataList?.let { MyAdapter(it, R.layout.payment_history, 0, 0, true) } ?: MyAdapter(emptyList(), R.layout.payment_history, 0, 0, true)
        recyclerView.adapter = adapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}