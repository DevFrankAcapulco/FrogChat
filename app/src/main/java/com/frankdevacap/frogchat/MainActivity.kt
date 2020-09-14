package com.frankdevacap.frogchat


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.frankdevacap.frogchat.adapters.PagerAdapter
import com.frankdevacap.frogchat.fragments.ChatFragment
import com.frankdevacap.frogchat.fragments.ConfigsFragment
import com.frankdevacap.frogchat.fragments.ContactsFragment
import com.frankdevacap.mylibrary.interfaces.ToolBarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolBarActivity() {

    private lateinit var adapter: PagerAdapter
    private var prevBottomSelected: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarToLoad(toolbarView as Toolbar)

        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()


    }

    private fun getPagerAdapter(): PagerAdapter {
        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(ContactsFragment())
        adapter.addFragment(ChatFragment())
        adapter.addFragment(ConfigsFragment())
        return adapter

    }

    private fun setUpViewPager(adapter: PagerAdapter) {
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    bottomNavigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }


        })

    }

    private fun setUpBottomNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_contacts -> {
                    viewPager.currentItem = 0; true
                }
                R.id.bottom_nav_chat -> {
                    viewPager.currentItem = 1; true
                }
                R.id.bottom_nav_configs -> {
                    viewPager.currentItem = 2; true
                }
                else -> false


            }
        }

    }

}