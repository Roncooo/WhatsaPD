package it.unipd.dei.esp.whatsapd

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import it.unipd.dei.esp.whatsapd.databinding.ActivityMainBinding
import it.unipd.dei.esp.whatsapd.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourites, R.id.nav_near_me
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar_home, menu)
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Ottieni il fragment corrente

        when (supportFragmentManager.fragments[0].childFragmentManager.fragments[0]) { //current fragment
            is PoiFragment -> {

                menu.findItem(R.id.favorite)?.isVisible = true
                menu.findItem(R.id.search)?.isVisible = false
            }

            is HomeFragment -> {

                menu.findItem(R.id.favorite)?.isVisible = false
                menu.findItem(R.id.search)?.isVisible = true
            }

            else -> {
                // Altri fragment, nascondi entrambe le icone
                menu.findItem(R.id.favorite)?.isVisible = false
                menu.findItem(R.id.search)?.isVisible = false
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
