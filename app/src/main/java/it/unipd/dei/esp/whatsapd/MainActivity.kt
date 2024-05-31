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
import it.unipd.dei.esp.whatsapd.ui.poi.PoiFragment

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
		
		// Passing menu IDs as a set
		appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.nav_home, R.id.nav_favourites, R.id.nav_near_me
			), drawerLayout
		)
		
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView.setupWithNavController(navController)
	}
	
	/**
	 * Inflates the [menu] in [R.menu.top_app_bar_home]; this adds items to the action bar if it is present.
	 */
	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.top_app_bar_home, menu)
		return true
	}
	
	/**
	 * Sets the icons in the app bar for each fragment.
	 */
	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		
		when (supportFragmentManager.fragments[0].childFragmentManager.fragments[0]) {
			is PoiFragment -> {
				
				menu.findItem(R.id.favorite)?.isVisible = true
				menu.findItem(R.id.search)?.isVisible = false
			}
			
			is HomeFragment -> {
				menu.findItem(R.id.favorite)?.isVisible = false
				menu.findItem(R.id.search)?.isVisible = true
			}
			
			else -> {
				// other fragments, hide both icons
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
