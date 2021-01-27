package com.victor.ko.scart.activities

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.victor.ko.scart.R
import com.victor.ko.scart.databinding.ActivityMainBinding
import io.paperdb.Paper


class MainActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityMainBinding
    private val accessToken = Paper.book().read<String>("accessToken") ?: ""
    //private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navView = bnd.navView
        val navBottom = bnd.navBottom

        navView.setupWithNavController(navController)
        navBottom.setupWithNavController(navController)

        navBottom.itemIconTintList = null

        bnd.title.typeface = ResourcesCompat.getFont(this, R.font.montserrat_medium)

        bnd.menuIcon.let { mb ->
            mb.setOnClickListener {
                bnd.drawerLayout.openDrawer(GravityCompat.START, true)
            }
        }

        navView.setNavigationItemSelectedListener { it ->

            val status = NavigationUI.onNavDestinationSelected(it, navController)

            /* Если успешно перешли на новый Fragment - скрываем боковую панель */
            if (status) {
                closeNavigationView()
            }

            /* Если такой же menuItem есть и на нижнем меню - ставим его как активный */
            //todo
            val item = navBottom.menu.findItem(it.itemId)
            navBottom.selectedItemId = item.itemId

            return@setNavigationItemSelectedListener status
        }

        navBottom.setOnNavigationItemSelectedListener {
            val status = NavigationUI.onNavDestinationSelected(it, navController)

            /* Если такой же menuItem есть и в боковом меню - ставим его как активный */
            val item = navView.menu.findItem(it.itemId)
            navView.setCheckedItem(item?.itemId ?: 0)

            return@setOnNavigationItemSelectedListener status
        }

        //loadBonuses()
        //loadPersonalInfo()
        //loadCartInfo()
    }

    private fun closeNavigationView() {
        bnd.drawerLayout.closeDrawer(GravityCompat.START, true)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host_fragment).popBackStack()) {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        //EventBus.getDefault().register(this)
    }

    override fun onStop() {
        //EventBus.getDefault().unregister(this)
        super.onStop()
    }
/*
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(errorEvent: ErrorEvent) {
        ErrorHandler.connectionError(this, errorEvent.e)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(errorEvent: ApiError) {
        ErrorHandler.apiError(this, errorEvent.error)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(cartInfo: UpdateCart) {
        setCartTotalPrice(cartInfo.totalPrice, cartInfo.itemsCount)
        setCartItemsCount(cartInfo.itemsCount)
    }*/

}
