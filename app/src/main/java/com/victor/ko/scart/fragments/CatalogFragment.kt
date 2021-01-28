package com.victor.ko.scart.fragments

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.victor.ko.scart.R
import com.victor.ko.scart.databinding.FragmentCatalogBinding
import com.victor.ko.scart.models.CatalogItem
import com.victor.ko.scart.models.CatalogSuccess
import com.victor.ko.scart.network.ApiAdapter
import com.victor.ko.scart.utils.toast
import io.paperdb.Paper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.dip
import today.e_bash.cityrose.events.ErrorEvent
import today.e_bash.cityrose.model.Catalog
import today.e_bash.cityrose.model.CatalogItem
import today.e_bash.cityrose.model.CatalogSuccess
import today.e_bash.cityrose.tools.EBashApi


class CatalogFragment : SFragment() {

    val TAG = "CatalogFragment"
    private lateinit var bnd: FragmentCatalogBinding
    private var binding: FragmentCatalogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val bnd1 get() = binding!!

    //private val compositeDisposable = CompositeDisposable()
    private val accessToken: String = Paper.book().read<String>("accessToken") ?: "null"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.actionBarTitle = resources.getString(R.string.catalog)
        //return inflater.inflate(R.layout.fragment_catalog, container, false)
        bnd = FragmentCatalogBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCatalog()
    }

//*****************************************/ RecyclerView /**************************//

    class CatalogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView? = view.previewImage
        val title: TextView? = view.title
        val price: TextView? = view.price
        val discount: TextView? = view.discount
    }

    class MoreItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class CatalogAdapter(private val catalogItem: CatalogItem) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        enum class CatalogViewTypes(val type: Int) {
            DEFAULT(0),
            LAST_ITEM(1)
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                catalogItem.products?.size -> CatalogViewTypes.LAST_ITEM.type
                else -> super.getItemViewType(position)
            }
        }

        init {
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                CatalogViewTypes.LAST_ITEM.type -> MoreItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_more, parent, false)
                )
                else -> CatalogViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_product, parent, false)
                )
            }
        }

        override fun getItemCount(): Int {
            return catalogItem.products?.size?.plus(1) ?: 0
        }

        override fun onBindViewHolder(p: RecyclerView.ViewHolder, position: Int) {

            if (p is CatalogViewHolder) {
                try {
                    val item = catalogItem.products?.get(position) ?: return
                    p.title?.text = item.title
                    p.title?.textColor = Color.parseColor("#000000")
                    p.discount?.context?.let { ctx ->
                        p.discount.typeface = ResourcesCompat.getFont(ctx, R.font.montserrat_bold)
                    }
                    p.price?.context?.let { ctx ->
                        p.price.typeface = ResourcesCompat.getFont(ctx, R.font.montserrat_semibold)
                    }

                    val priceStringFormat = p.itemView.context?.getString(R.string.price).toString()
                    when {
                        item.discount?.toFloat() == item.price?.toFloat() || item.discount?.toFloat() == 0F -> {
                            p.price?.textColor = Color.parseColor("#000000")
                            p.price?.text = String.format(priceStringFormat, item.price)
                            p.discount?.visibility = View.GONE
                        }
                        item.discount?.toFloat()?.compareTo(item.price?.toFloat() ?: 0f) ?: 0 < 0 -> {
                            p.price?.textColor = Color.parseColor("#C4C4C4")
                            p.discount?.textColor = Color.parseColor("#000000")
                            p.discount?.text = String.format(priceStringFormat, item.discount)
                            p.price?.text = String.format(priceStringFormat, item.price)
                            p.price?.paintFlags = p.price?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG)!!
                        }
                        else -> {
                            p.price?.visibility = View.GONE
                            p.discount?.visibility = View.GONE
                        }
                    }

                    p.image?.let {
                        it.scaleType = ImageView.ScaleType.FIT_XY
                        //Glide.with(it).load(item.image).into(it)
                        Glide.with(it).load(Uri.parse("file:///android_asset/mocks/img/"+item.image)).into(it)
                    }

                    p.itemView.onClick {
                        try {
                            val args = Bundle()
                            args.putSerializable("product", Gson().toJson(item))
                            p.itemView.findNavController()
                                .navigate(R.id.action_catalogFragment_to_productFragment, args)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (p is MoreItemViewHolder) {
                p.itemView.onClick {
                    val args = Bundle()
                    args.putSerializable("catalogItem", Gson().toJson(catalogItem))
                    p.itemView.findNavController()
                        .navigate(R.id.action_catalogFragment_to_fullCatalogFragment, args)
                }
            }
        }
    }

    private fun showCatalog(catalogSuccess: CatalogSuccess) {
        try {
            bnd.catalogRootView.removeAllViews()
            catalogSuccess.catalog?.forEach { c ->
                val titleLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                titleLayoutParams.setMargins(14, 30, 14, 0)

                val subtitleLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                subtitleLayoutParams.setMargins(12, 0, 12, 0)

                bnd.catalogRootView.verticalLayout {
                    verticalLayout {
                        onClick {
                            try {
                                val args = Bundle()
                                args.putSerializable("catalogItem", Gson().toJson(c))
                                this@CatalogFragment.findNavController()
                                    .navigate(R.id.action_catalogFragment_to_fullCatalogFragment, args)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        if (!c?.title.isNullOrEmpty()) {
                            textView {
                                text = c?.title
                                textSize = 16f
                                textColor = Color.BLACK
                                typeface = ResourcesCompat.getFont(this.context, R.font.montserrat_semibold)
                                layoutParams = titleLayoutParams
                            }
                        }

                        if (!c?.description.isNullOrEmpty()) {
                            textView {
                                text = c?.description
                                textSize = 10f
                                textColor = Color.parseColor("#787878")
                                typeface = ResourcesCompat.getFont(this.context, R.font.montserrat_semibold)
                                layoutParams = subtitleLayoutParams
                            }
                        }
                    }

                    if (!c?.products.isNullOrEmpty()) {
                        recyclerView {
                            layoutManager = LinearLayoutManager(
                                activity,
                                LinearLayout.HORIZONTAL, false
                            )

                            if(c != null )
                                adapter = CatalogAdapter(c)

                            addItemDecoration(object : RecyclerView.ItemDecoration() {
                                override fun getItemOffsets(
                                    outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State
                                ) {
                                    outRect.left = dip(12)
                                }
                            })
                        }.lparams {
                            topMargin = dip(17)
                        }
                    }

                    if (c != catalogSuccess.catalog.last()) {
                        view {
                            backgroundColor = Color.parseColor("#80BDCFD5")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(26)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        //compositeDisposable.dispose()
    }

    private fun updateCatalog() {
        CoroutineScope(Dispatchers.IO).launch() {
            try {
                val response = ApiAdapter.apiClient.catalog(accessToken)

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    when {
                        data.success != null -> {
                            showCatalog(data.success)
                            bnd.progressIndicator.visibility = View.GONE
                            bnd.scrollView.visibility = View.VISIBLE
                        }
                    }
                } else {
                    // Show API error.
                    // This is when the server responded with an error.
                    toast("Error Occurred: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                toast("Error Occurred: ${e.message}")
            }
        }
    }
}