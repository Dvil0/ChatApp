package com.dv.kotlin.chatapp.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.adapters.PagerAdapter
import com.dv.kotlin.chatapp.fragments.ChatFragment
import com.dv.kotlin.chatapp.fragments.InfoFragment
import com.dv.kotlin.chatapp.fragments.RatesFragment
import com.dv.kotlin.mylibraryaar.ToolbarActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: ToolbarActivity(){

    private var prevBottomSelected: MenuItem? = null

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        toolbarToLoad( toolbarView as Toolbar? )

        setUpViewPager( getPagerAdapter() )
        setUpBottomNavBar()
    }

    private fun getPagerAdapter(): PagerAdapter{
        val adapter = PagerAdapter( supportFragmentManager )
        adapter.addFragment( InfoFragment() )
        adapter.addFragment( RatesFragment() )
        adapter.addFragment( ChatFragment() )
        return adapter
    }

    private fun setUpViewPager( adapter: PagerAdapter ){
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener( object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if( prevBottomSelected == null ){
                    bottomNav.menu.getItem( 0 ).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                bottomNav.menu.getItem( position ).isChecked = true
                prevBottomSelected = bottomNav.menu.getItem( position )
            }
        })
    }

    private fun setUpBottomNavBar(){
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when( item.itemId ){
                R.id.bottom_nav_info -> { viewPager.currentItem = 0; true }
                R.id.bottom_nav_rates -> { viewPager.currentItem = 1; true }
                R.id.bottom_nav_chat -> { viewPager.currentItem = 2; true }
                else -> false
            }
        }
    }
}